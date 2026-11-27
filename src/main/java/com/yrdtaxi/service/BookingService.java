package com.yrdtaxi.service;

import com.yrdtaxi.model.Booking;
import com.yrdtaxi.model.Driver;
import com.yrdtaxi.repository.BookingRepository;
import com.yrdtaxi.repository.DriverRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private SmsService smsService;


    // ============================================================
    // YRD TAXI FARE SETTINGS
    // ============================================================

    private static final double MINIMUM_KM = 110.0;

    // ------------------------------------------------------------
    // ONE WAY RATES
    // ------------------------------------------------------------

    private static final double SWIFT_ONEWAY = 15.0;
    private static final double DIZRE_ONEWAY = 17.0;
    private static final double SUV_ONEWAY = 20.0;
    private static final double ERTIGA_ONEWAY = 22.0;

    // ------------------------------------------------------------
    // ROUND TRIP RATES
    // ------------------------------------------------------------

    private static final double SWIFT_ROUND = 14.0;
    private static final double DIZRE_ROUND = 16.0;
    private static final double SUV_ROUND = 19.0;
    private static final double ERTIGA_ROUND = 21.0;

    // ------------------------------------------------------------
    // DRIVER BATA
    // ------------------------------------------------------------

    private static final double DRIVER_BATA_PER_DAY = 550.0;

    // ------------------------------------------------------------
    // SHORT ROUND TRIP
    // ------------------------------------------------------------

    private static final double SHORT_ROUND_LIMIT_KM = 75.0;
    private static final double SHORT_ROUND_CHARGE = 600.0;


    // ============================================================
    // CREATE BOOKING
    // ============================================================

    public Booking createBooking(Booking booking) {

        booking.setBookingTime(LocalDateTime.now());

        String tripType = normalize(booking.getTripType());
        String cabType = normalize(booking.getCabType());

        // --------------------------------------------------------
        // ORIGINAL ONE-WAY DISTANCE
        // --------------------------------------------------------

        double oneWayDistance = Math.ceil(
                Math.max(0.0, booking.getDistance())
        );

        if (oneWayDistance <= 0) {

            throw new RuntimeException(
                    "Distance must be greater than 0 KM"
            );
        }


        // ========================================================
        // FARE VARIABLES
        // ========================================================

        double billingDistance;
        double cabFare;
        double driverBata = 0.0;
        double tollFees;

        boolean shortRoundTrip =
                "ROUND".equals(tripType)
                && oneWayDistance < SHORT_ROUND_LIMIT_KM;


        // ========================================================
        // SHORT ROUND TRIP
        // ========================================================

        if (shortRoundTrip) {

            /*
             * YRD TAXI RULE:
             *
             * ROUND < 75 KM
             *
             * Minimum one-way distance = 110 KM
             * One-way rate is used
             * Add ₹600 short-round charge
             * Driver Bata = ₹0
             */

            billingDistance = oneWayDistance * 2.0;

            double oneWayRate =
                    getRatePerKm(
                            "ONEWAY",
                            cabType
                    );

            double chargeableOneWayKm =
                    Math.max(
                            oneWayDistance,
                            MINIMUM_KM
                    );

            double oneWayFare =
                    chargeableOneWayKm
                    * oneWayRate;

            cabFare =
                    oneWayFare
                    + SHORT_ROUND_CHARGE;

            // IMPORTANT:
            // Short round trip has NO driver bata.
            driverBata = 0.0;

        }


        // ========================================================
        // NORMAL ROUND TRIP
        // ========================================================

        else if ("ROUND".equals(tripType)) {

            /*
             * Example:
             *
             * Thanjavur -> Chennai
             * One way = 340 KM
             *
             * Billing distance:
             *
             * 340 × 2 = 680 KM
             */

            billingDistance =
                    oneWayDistance * 2.0;

            double ratePerKm =
                    getRatePerKm(
                            "ROUND",
                            cabType
                    );

            /*
             * ROUND fare uses the complete round-trip
             * billing distance.
             *
             * Minimum billing distance = 110 KM
             */

            double chargeableDistance =
                    Math.max(
                            billingDistance,
                            MINIMUM_KM
                    );

            cabFare =
                    chargeableDistance
                    * ratePerKm;


            // ----------------------------------------------------
            // DRIVER BATA
            // ----------------------------------------------------

            int days =
                    calculateDays(
                            booking.getPickupDate(),
                            booking.getReturnDate()
                    );

            driverBata =
                    days
                    * DRIVER_BATA_PER_DAY;
        }



        // ========================================================
        // ONE WAY
        // ========================================================
        else {
            /*
             * ONE WAY RULE:
             * Actual distance is used for driver bata.
             * Minimum billing distance = 110 KM.
             *
             * 0-100 KM  = ₹0 driver bata
             * Above 100 KM = ₹550 driver bata
             */

            billingDistance = oneWayDistance;

            double ratePerKm = getRatePerKm(
                    "ONEWAY",
                    cabType
            );

            double chargeableDistance = Math.max(
                    billingDistance,
                    MINIMUM_KM
            );

            cabFare = chargeableDistance * ratePerKm;

            // Driver bata is based on actual one-way distance
            driverBata = oneWayDistance > 100
                    ? DRIVER_BATA_PER_DAY
                    : 0.0;
        }


        // ========================================================
        // TOLL FEES - EXTRA CHARGE
        // ========================================================
        tollFees = Math.max(
                0.0,
                booking.getTollFees() == null
                        ? 0.0
                        : booking.getTollFees()
        );
        

        // ========================================================
        // FINAL TOTAL
        // ========================================================
        double totalFare =
                cabFare
                + driverBata
                + tollFees;

        booking.setDistance(roundMoney(billingDistance));
        booking.setBaseFare(roundMoney(cabFare));
        booking.setDriverBata(roundMoney(driverBata));
        booking.setTollFees(roundMoney(tollFees));
        booking.setSurgeMultiplier(1.0);
        booking.setTotalFare(roundMoney(totalFare));

        // ========================================================
        // SAVE CORRECT FARE VALUES
        // ========================================================

        booking.setDistance(
                roundMoney(billingDistance)
        );

        booking.setBaseFare(
                roundMoney(cabFare)
        );

        booking.setDriverBata(
                roundMoney(driverBata)
        );

        booking.setTollFees(
                roundMoney(tollFees)
        );

        booking.setSurgeMultiplier(1.0);

        booking.setTotalFare(
                roundMoney(totalFare)
        );


        // ========================================================
        // OTP
        // ========================================================

        String otp =
                String.valueOf(
                        (int) (Math.random() * 9000) + 1000
                );

        booking.setRideOtp(otp);
        booking.setOtpVerified(false);


        // ========================================================
        // DRIVER ASSIGNMENT
        // ========================================================

        Driver driver =
                driverRepository
                        .findFirstByOnlineTrueAndAvailableTrue()
                        .orElse(null);

        if (driver != null) {

            booking.setDriver(driver);

            booking.setStatus(
                    "DRIVER_ASSIGNED"
            );

            driver.setAvailable(false);

            driverRepository.save(driver);

        } else {

            booking.setStatus(
                    "REQUESTED"
            );
        }


        // ========================================================
        // SAVE BOOKING
        // ========================================================

        Booking savedBooking =
                bookingRepo.save(booking);


        // ========================================================
        // SMS
        // ========================================================

        try {

            String message =
                    "YRD Taxi Booking Confirmed!\n" +
                    "Pickup: " +
                    booking.getPickupLocation() + "\n" +
                    "Drop: " +
                    booking.getDropLocation() + "\n" +
                    "Trip Type: " +
                    booking.getTripType() + "\n" +
                    "Cab Type: " +
                    booking.getCabType() + "\n" +
                    "Distance: " +
                    booking.getDistance() +
                    " KM\n" +
                    "Base Fare: Rs." +
                    booking.getBaseFare() + "\n" +
                    "Driver Bata: Rs." +
                    booking.getDriverBata() + "\n" +
                    "Toll Fees: Rs." +
                    booking.getTollFees() + "\n" +
                    "Total Fare: Rs." +
                    booking.getTotalFare();

            if (driver != null) {

                message +=
                        "\nDriver: " +
                        driver.getName() +
                        " (" +
                        driver.getPhone() +
                        ")";
            }

            smsService.sendSms(
                    booking.getCustomerPhone(),
                    message
            );

        } catch (Exception e) {

            System.out.println(
                    "SMS failed: " +
                    e.getMessage()
            );
        }


        return savedBooking;
    }


    // ============================================================
    // VEHICLE RATE
    // ============================================================

    private double getRatePerKm(
            String tripType,
            String cabType
    ) {

        if ("ROUND".equals(tripType)) {

            switch (cabType) {

                case "SWIFT":
                    return SWIFT_ROUND;

                case "DIZRE":
                    return DIZRE_ROUND;

                case "SUV":
                    return SUV_ROUND;

                case "ERTIGA":
                    return ERTIGA_ROUND;

                default:
                    throw new RuntimeException(
                            "Invalid cab type: " + cabType
                    );
            }
        }


        switch (cabType) {

            case "SWIFT":
                return SWIFT_ONEWAY;

            case "DIZRE":
                return DIZRE_ONEWAY;

            case "SUV":
                return SUV_ONEWAY;

            case "ERTIGA":
                return ERTIGA_ONEWAY;

            default:
                throw new RuntimeException(
                        "Invalid cab type: " + cabType
                );
        }
    }


    // ============================================================
    // CALCULATE DAYS
    // ============================================================

    private int calculateDays(
            LocalDate pickupDate,
            LocalDate returnDate
    ) {

        if (pickupDate == null || returnDate == null) {
            return 1;
        }

        long days =
                ChronoUnit.DAYS.between(
                        pickupDate,
                        returnDate
                );

        /*
         * Same-day trip = 1 day
         *
         * Example:
         * 2026-10-08 -> 2026-10-08
         * = 1 day
         *
         * 2026-10-08 -> 2026-10-09
         * = 1 day
         *
         * 2026-10-08 -> 2026-10-10
         * = 2 days
         */

        return (int) Math.max(1, days);
    }


    // ============================================================
    // NORMALIZE
    // ============================================================

    private String normalize(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .toUpperCase();
    }


    // ============================================================
    // ROUND MONEY
    // ============================================================

    private double roundMoney(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }


    // ============================================================
    // ACCEPT RIDE
    // ============================================================

    public Booking acceptRide(
            Long bookingId,
            Long driverId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        Driver driver =
                driverService.getDriverById(
                        driverId
                );

        booking.setDriver(driver);

        booking.setStatus(
                "DRIVER_ASSIGNED"
        );

        driver.setAvailable(false);

        driverRepository.save(driver);

        return bookingRepo.save(booking);
    }


    // ============================================================
    // START RIDE
    // ============================================================

    public Booking startRide(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                booking.getOtpVerified())) {

            throw new RuntimeException(
                    "Please verify Ride OTP before starting the ride."
            );
        }

        booking.setStatus("STARTED");

        booking.setStartTime(
                LocalDateTime.now()
        );

        return bookingRepo.save(booking);
    }


    // ============================================================
    // COMPLETE RIDE
    // ============================================================

    public Booking completeRide(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (!Boolean.TRUE.equals(
                booking.getOtpVerified())) {

            throw new RuntimeException(
                    "Please verify Ride OTP before completing the ride."
            );
        }

        booking.setStatus("COMPLETED");

        booking.setEndTime(
                LocalDateTime.now()
        );

        Driver driver =
                booking.getDriver();

        if (driver != null) {

            driver.setAvailable(true);

            driverRepository.save(driver);
        }

        return bookingRepo.save(booking);
    }


    // ============================================================
    // GET ALL BOOKINGS
    // ============================================================

    public List<Booking> getAllBookings() {

        return bookingRepo.findAll();
    }


    // ============================================================
    // GET BOOKING
    // ============================================================

    public Booking getBookingById(
            Long id
    ) {

        return bookingRepo.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Booking not found"
                        )
                );
    }


    // ============================================================
    // CUSTOMER BOOKINGS
    // ============================================================

    public List<Booking> getBookingsByPhone(
            String phone
    ) {

        return bookingRepo
                .findByCustomerPhone(phone);
    }


    public List<Booking> getBookingsByEmail(
            String email
    ) {

        return bookingRepo
                .findByCustomerEmail(email);
    }


    // ============================================================
    // STATUS
    // ============================================================

    public List<Booking> getBookingsByStatus(
            String status
    ) {

        return bookingRepo.findByStatus(status);
    }


    // ============================================================
    // ANALYTICS
    // ============================================================

    public long getTotalBookings() {

        return bookingRepo.count();
    }


    public long getBookingCountByStatus(
            String status
    ) {

        return bookingRepo
                .findByStatus(status)
                .size();
    }


    public double getTotalRevenue() {

        return bookingRepo
                .findByStatus("PAID")
                .stream()
                .mapToDouble(
                        Booking::getTotalFare
                )
                .sum();
    }


    // ============================================================
    // INVOICE
    // ============================================================

    public String generateInvoice(
            Long bookingId
    ) {

        Booking booking =
                getBookingById(bookingId);

        return
                "YRD TAXI INVOICE\n" +
                "------------------------\n" +
                "Booking ID: " +
                booking.getId() + "\n" +

                "Customer: " +
                booking.getCustomerName() + "\n" +

                "Phone: " +
                booking.getCustomerPhone() + "\n" +

                "Pickup: " +
                booking.getPickupLocation() + "\n" +

                "Drop: " +
                booking.getDropLocation() + "\n" +

                "Trip Type: " +
                booking.getTripType() + "\n" +

                "Cab Type: " +
                booking.getCabType() + "\n" +

                "Distance: " +
                booking.getDistance() +
                " km\n" +

                "Base Fare: Rs." +
                booking.getBaseFare() + "\n" +

                "Driver Bata: Rs." +
                booking.getDriverBata() + "\n" +

                "Toll Fees: Rs." +
                booking.getTollFees() + "\n" +

                "Total Fare: Rs." +
                booking.getTotalFare() + "\n" +

                "Payment Status: " +
                booking.getStatus();
    }


    // ============================================================
    // DRIVER ACCEPT
    // ============================================================

    public Booking driverAcceptBooking(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        booking.setStatus("ACCEPTED");

        booking.setEtaMinutes(15);

        return bookingRepo.save(booking);
    }


    // ============================================================
    // DRIVER REJECT
    // ============================================================

    public Booking driverRejectBooking(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        Driver driver =
                booking.getDriver();

        if (driver != null) {

            driver.setAvailable(true);

            driverRepository.save(driver);
        }

        booking.setDriver(null);

        booking.setStatus("REQUESTED");

        return bookingRepo.save(booking);
    }


    // ============================================================
    // CANCEL
    // ============================================================

    public Booking cancelRide(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if ("STARTED".equals(
                booking.getStatus())
                || "COMPLETED".equals(
                booking.getStatus())
                || "PAID".equals(
                booking.getStatus())) {

            throw new RuntimeException(
                    "Trip already started/completed. Cannot cancel."
            );
        }

        booking.setStatus("CANCELLED");

        Driver driver =
                booking.getDriver();

        if (driver != null) {

            driver.setAvailable(true);

            driverRepository.save(driver);
        }

        return bookingRepo.save(booking);
    }


    // ============================================================
    // VERIFY OTP
    // ============================================================

    public Booking verifyRideOtp(
            Long bookingId,
            String otp
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        if (otp == null
                || !otp.trim().equals(
                booking.getRideOtp())) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }

        booking.setOtpVerified(true);

        return bookingRepo.save(booking);
    }


    // ============================================================
    // DRIVER REACHED
    // ============================================================

    public Booking driverReached(
            Long bookingId
    ) {

        Booking booking =
                bookingRepo.findById(bookingId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Booking not found"
                                )
                        );

        booking.setStatus(
                "DRIVER_REACHED"
        );

        return bookingRepo.save(booking);
    }
}






        









