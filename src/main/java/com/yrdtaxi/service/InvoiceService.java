package com.yrdtaxi.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yrdtaxi.model.Booking;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class InvoiceService {
	
	
	  private String formatInvoiceDate(LocalDateTime date) {
          if (date == null) {
              return "N/A";
          }

          return date.format(
              DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
          );
      }

      private String formatPickupDate(LocalDate date) {
          if (date == null) {
              return "N/A";
          }

          return date.format(
              DateTimeFormatter.ofPattern("dd-MM-yyyy")
          );
      }   
	
	
	

    public byte[] generateInvoicePdf(Booking booking) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4, 36, 36, 40, 40);

            PdfWriter.getInstance(document, outputStream);

            document.open();
            
          
          

            // =========================================================
            // YRD TAXI LOGO
            // =========================================================
            try {
                ClassPathResource resource =
                        new ClassPathResource("static/logo.png");

                if (resource.exists()) {

                    try (InputStream inputStream = resource.getInputStream()) {

                        Image logo = Image.getInstance(
                                inputStream.readAllBytes()
                        );

                        logo.scaleAbsolute(80, 80);
                        logo.setAlignment(Element.ALIGN_CENTER);

                        document.add(logo);
                    }

                } else {
                    System.out.println(
                            "YRD TAXI logo not found: static/logo.png"
                    );
                }

            } catch (Exception e) {
                System.out.println(
                        "Unable to load YRD TAXI logo: " + e.getMessage()
                );
            }

            // =========================================================
            // TITLE
            // =========================================================
            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20,
                    BaseColor.BLACK
            );

            Paragraph title = new Paragraph(
                    "YRD TAXI",
                    titleFont
            );

            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Font subtitleFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    10,
                    BaseColor.DARK_GRAY
            );

            Paragraph subtitle = new Paragraph(
                    "VALGA VALAMUDAN - TRUSTED RIDES",
                    subtitleFont
            );

            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);

            document.add(new Paragraph(" "));

            // =========================================================
            // INVOICE TITLE
            // =========================================================
            Font invoiceFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    16,
                    BaseColor.BLACK
            );

            Paragraph invoiceTitle = new Paragraph(
                    "TAXI BOOKING INVOICE",
                    invoiceFont
            );

            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(invoiceTitle);

            document.add(new Paragraph(" "));
            
            // =========================================================
            // INVOICE DATE
            // ========================================================= 
           
            Font dateFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    10,
                    BaseColor.DARK_GRAY
            );

            String invoiceDateText = booking.getBookingTime() != null
                    ? formatInvoiceDate(booking.getBookingTime())
                    : "N/A";

            Paragraph invoiceDate = new Paragraph(
                    "Invoice Date: " + invoiceDateText,
                    dateFont
            );

            invoiceDate.setAlignment(Element.ALIGN_RIGHT);
            document.add(invoiceDate);

            document.add(new Paragraph(" "));
           

            

            // =========================================================
            // CUSTOMER / BOOKING DETAILS
            // =========================================================
            PdfPTable detailsTable = new PdfPTable(2);

            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{35f, 65f});

            addDetail(detailsTable, "Booking ID",
                    String.valueOf(booking.getId()));

            addDetail(
                    detailsTable,
                    "Customer Name",
                    safe(booking.getCustomerName())
            );

            addDetail(
                    detailsTable,
                    "Mobile",
                    safe(booking.getCustomerPhone())
            );

            addDetail(
                    detailsTable,
                    "Email",
                    safe(booking.getCustomerEmail())
            );

            addDetail(detailsTable, "Pickup",
                    safe(booking.getPickupLocation()));

            addDetail(detailsTable, "Drop",
                    safe(booking.getDropLocation()));

            addDetail(detailsTable, "Trip Type",
                    safe(booking.getTripType()));

            addDetail(detailsTable, "Cab Type",
                    safe(booking.getCabType()));

            addDetail(detailsTable, "Distance",
                    String.valueOf(booking.getDistance()) + " KM");

            addDetail(detailsTable, "Booking Status",
                    booking.getStatus() != null
                            ? booking.getStatus().toString()
                            : "N/A");
            
            addDetail(
                    detailsTable,
                    "Pickup Date",
                    formatPickupDate(booking.getPickupDate())
            );

            addDetail(
                    detailsTable,
                    "Return Date",
                    formatPickupDate(booking.getReturnDate())
            );

            document.add(detailsTable);

            document.add(new Paragraph(" "));

            // =========================================================
            // FARE DETAILS
            // =========================================================
            Font sectionFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    13,
                    BaseColor.BLACK
            );

            Paragraph fareHeading = new Paragraph(
                    "FARE DETAILS",
                    sectionFont
            );

            document.add(fareHeading);
            document.add(new Paragraph(" "));

            PdfPTable fareTable = new PdfPTable(2);

            fareTable.setWidthPercentage(100);
            fareTable.setWidths(new float[]{65f, 35f});

            addFareRow(
                    fareTable,
                    "Base Fare",
                    booking.getBaseFare()
            );

            addFareRow(
                    fareTable,
                    "Driver Bata",
                    booking.getDriverBata()
            );

            addFareRow(
                    fareTable,
                    "Toll Fees",
                    booking.getTollFees()
            );

            // =========================================================
            // TOTAL FARE
            // =========================================================
            double baseFare = booking.getBaseFare();
            double driverBata = booking.getDriverBata();
            double tollFees = booking.getTollFees();

            double storedTotalFare = booking.getTotalFare();

            /*
             * Use the stored total when it is valid.
             * Otherwise calculate:
             *
             * Base Fare + Driver Bata + Toll Fees
             */
            double totalFare = storedTotalFare;

            if (totalFare <= 0) {
                totalFare =
                        baseFare
                        + driverBata
                        + tollFees;
            }

            addTotalRow(
                    fareTable,
                    "TOTAL FARE",
                    totalFare
            );

            document.add(fareTable);

            document.add(new Paragraph(" "));

            // =========================================================
            // PAYMENT INFORMATION
            // =========================================================
            Paragraph paymentHeading = new Paragraph(
                    "PAYMENT INFORMATION",
                    sectionFont
            );

            document.add(paymentHeading);
            document.add(new Paragraph(" "));

            PdfPTable paymentTable = new PdfPTable(2);

            paymentTable.setWidthPercentage(100);
            paymentTable.setWidths(new float[]{35f, 65f});

            addDetail(
                    paymentTable,
                    "Payment Status",
                    booking.getPaid() ? "PAID" : "PENDING"
            );

            document.add(paymentTable);

            document.add(new Paragraph(" "));

            // =========================================================
            // FOOTER
            // =========================================================
            Font footerFont = FontFactory.getFont(
                    FontFactory.HELVETICA,
                    9,
                    BaseColor.DARK_GRAY
            );

            Paragraph footer = new Paragraph(
                    "Thank you for choosing YRD TAXI.\n" +
                    "VALGA VALAMUDAN - TRUSTED RIDES",
                    footerFont
            );

            footer.setAlignment(Element.ALIGN_CENTER);

            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate YRD TAXI invoice: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =============================================================
    // DETAIL ROW
    // =============================================================
    private void addDetail(
            PdfPTable table,
            String label,
            String value
    ) {

        Font labelFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10
        );

        Font valueFont = FontFactory.getFont(
                FontFactory.HELVETICA,
                10
        );

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(label, labelFont)
                );

        PdfPCell valueCell =
                new PdfPCell(
                        new Phrase(value, valueFont)
                );

        labelCell.setPadding(6);
        valueCell.setPadding(6);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // =============================================================
    // FARE ROW
    // =============================================================
    private void addFareRow(
            PdfPTable table,
            String label,
            double amount
    ) {

        Font font = FontFactory.getFont(
                FontFactory.HELVETICA,
                10
        );

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(label, font)
                );

        PdfPCell amountCell =
                new PdfPCell(
                        new Phrase(
                                "Rs. " + String.format("%.2f", amount),
                                font
                        )
                );

        labelCell.setPadding(7);
        amountCell.setPadding(7);

        amountCell.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        table.addCell(labelCell);
        table.addCell(amountCell);
    }

    // =============================================================
    // TOTAL ROW
    // =============================================================
    private void addTotalRow(
            PdfPTable table,
            String label,
            double amount
    ) {

        Font totalFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                12
        );

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(label, totalFont)
                );

        PdfPCell amountCell =
                new PdfPCell(
                        new Phrase(
                                "Rs. " +
                                String.format("%.2f", amount),
                                totalFont
                        )
                );

        labelCell.setPadding(8);
        amountCell.setPadding(8);

        amountCell.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        table.addCell(labelCell);
        table.addCell(amountCell);
    }

    // =============================================================
    // SAFE STRING
    // =============================================================
    private String safe(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "N/A";
        }

        return value;
    }
}


