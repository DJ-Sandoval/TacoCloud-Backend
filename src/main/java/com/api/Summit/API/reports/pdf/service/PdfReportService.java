package com.api.Summit.API.reports.pdf.service;

import com.api.Summit.API.view.dto.ClienteReportDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfReportService {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);

    public byte[] generateClientesReport(ClienteReportDTO reportDTO) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Crear tabla para el encabezado (imagen y texto)
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});

            // Celda para la imagen
            try {
                Image logo = Image.getInstance(new ClassPathResource("static/reporte.png").getURL());
                logo.scaleToFit(80, 80);
                PdfPCell imageCell = new PdfPCell(logo, true);
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                imageCell.setPadding(5);
                headerTable.addCell(imageCell);
            } catch (IOException e) {
                log.warn("No se pudo cargar la imagen del logo, usando celda vacía");
                PdfPCell emptyCell = new PdfPCell();
                emptyCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(emptyCell);
            }

            // Celda para la información del reporte
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER);
            infoCell.setPadding(5);

            Paragraph title = new Paragraph("REPORTE DE CLIENTES", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);

            Paragraph date = new Paragraph("Fecha: " + reportDTO.getFechaGeneracion(), NORMAL_FONT);
            date.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitle = new Paragraph(reportDTO.getTitulo(), NORMAL_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);

            infoCell.addElement(title);
            infoCell.addElement(date);
            infoCell.addElement(subtitle);
            headerTable.addCell(infoCell);

            document.add(headerTable);
            document.add(new Paragraph(" ")); // Espacio en blanco

            // Crear tabla para los datos de clientes
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 3, 3, 2});

            // Encabezados de la tabla con color azul
            addTableHeader(table, "ID");
            addTableHeader(table, "NOMBRE");
            addTableHeader(table, "TELÉFONO");
            addTableHeader(table, "EMAIL");
            addTableHeader(table, "DIRECCIÓN");
            addTableHeader(table, "FRECUENTE");

            // Datos de los clientes
            for (var cliente : reportDTO.getClientes()) {
                addTableRow(table, cliente.getId().toString());
                addTableRow(table, cliente.getNombre());
                addTableRow(table, cliente.getTelefono() != null ? cliente.getTelefono() : "");
                addTableRow(table, cliente.getEmail() != null ? cliente.getEmail() : "");
                addTableRow(table, cliente.getDireccion() != null ? cliente.getDireccion() : "");
                addTableRow(table, cliente.isFrecuente() ? "SÍ" : "NO");
            }

            document.add(table);

            // Agregar resumen al final
            document.add(new Paragraph(" "));
            Paragraph summary = new Paragraph(
                    String.format("Total de clientes: %d | Clientes frecuentes: %d",
                            reportDTO.getTotalClientes(),
                            reportDTO.getTotalFrecuentes()),
                    NORMAL_FONT
            );
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generando reporte PDF", e);
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage());
        }
    }

    private void addTableHeader(PdfPTable table, String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
        cell.setBackgroundColor(new BaseColor(51, 122, 183)); // Azul
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addTableRow(PdfPTable table, String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, NORMAL_FONT));
        cell.setPadding(5);
        table.addCell(cell);
    }
}
