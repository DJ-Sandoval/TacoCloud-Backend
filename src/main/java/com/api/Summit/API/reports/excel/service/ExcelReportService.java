package com.api.Summit.API.reports.excel.service;

import com.api.Summit.API.view.dto.CategoriaReportDTO;
import com.api.Summit.API.view.dto.ClienteReportDTO;
import com.api.Summit.API.view.dto.ProductoReportDTO;
import com.api.Summit.API.view.dto.ProductoReportDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class ExcelReportService {

    public byte[] generateClientesExcelReport(ClienteReportDTO reportDTO) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Crear hoja de trabajo
            Sheet sheet = workbook.createSheet("Reporte de Clientes");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle summaryStyle = createSummaryStyle(workbook);

            // Configurar anchos de columnas
            sheet.setColumnWidth(0, 2000);   // ID
            sheet.setColumnWidth(1, 8000);   // Nombre
            sheet.setColumnWidth(2, 5000);   // Teléfono
            sheet.setColumnWidth(3, 8000);   // Email
            sheet.setColumnWidth(4, 10000);  // Dirección
            sheet.setColumnWidth(5, 4000);   // Frecuente

            int currentRow = 0;

            // Título del reporte
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE DE CLIENTES - " + reportDTO.getTitulo());
            titleCell.setCellStyle(titleStyle);

            // Información del reporte
            Row infoRow1 = sheet.createRow(currentRow++);
            Cell infoCell1 = infoRow1.createCell(0);
            infoCell1.setCellValue("Fecha de generación: " + reportDTO.getFechaGeneracion());
            infoCell1.setCellStyle(normalStyle);

            if (reportDTO.getNegocioNombre() != null) {
                Row infoRow2 = sheet.createRow(currentRow++);
                Cell infoCell2 = infoRow2.createCell(0);
                infoCell2.setCellValue("Negocio: " + reportDTO.getNegocioNombre());
                infoCell2.setCellStyle(normalStyle);
            }

            currentRow++; // Espacio en blanco

            // Encabezados de la tabla
            Row headerRow = sheet.createRow(currentRow++);
            String[] headers = {"ID", "NOMBRE", "TELÉFONO", "EMAIL", "DIRECCIÓN", "FRECUENTE"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos de los clientes
            for (var cliente : reportDTO.getClientes()) {
                Row dataRow = sheet.createRow(currentRow++);

                dataRow.createCell(0).setCellValue(cliente.getId());
                dataRow.createCell(1).setCellValue(cliente.getNombre());
                dataRow.createCell(2).setCellValue(cliente.getTelefono() != null ? cliente.getTelefono() : "");
                dataRow.createCell(3).setCellValue(cliente.getEmail() != null ? cliente.getEmail() : "");
                dataRow.createCell(4).setCellValue(cliente.getDireccion() != null ? cliente.getDireccion() : "");
                dataRow.createCell(5).setCellValue(cliente.isFrecuente() ? "SÍ" : "NO");

                // Aplicar estilo normal a todas las celdas
                for (int i = 0; i < headers.length; i++) {
                    dataRow.getCell(i).setCellStyle(normalStyle);
                }
            }

            currentRow++; // Espacio en blanco

            // Resumen
            Row summaryRow1 = sheet.createRow(currentRow++);
            Cell summaryCell1 = summaryRow1.createCell(0);
            summaryCell1.setCellValue("RESUMEN:");
            summaryCell1.setCellStyle(summaryStyle);

            Row summaryRow2 = sheet.createRow(currentRow++);
            Cell summaryCell2 = summaryRow2.createCell(0);
            summaryCell2.setCellValue("Total de clientes: " + reportDTO.getTotalClientes());
            summaryCell2.setCellStyle(summaryStyle);

            Row summaryRow3 = sheet.createRow(currentRow++);
            Cell summaryCell3 = summaryRow3.createCell(0);
            summaryCell3.setCellValue("Clientes frecuentes: " + reportDTO.getTotalFrecuentes());
            summaryCell3.setCellStyle(summaryStyle);

            // Escribir el workbook al output stream
            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error generando reporte Excel", e);
            throw new RuntimeException("Error al generar el reporte Excel: " + e.getMessage());
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());

        style.setFont(font);
        return style;
    }

    private CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);

        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    public byte[] generateCategoriasExcelReport(CategoriaReportDTO reportDTO, String tipoReporte) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Crear hoja de trabajo
            Sheet sheet = workbook.createSheet("Reporte de Categorías");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle summaryStyle = createSummaryStyle(workbook);

            int currentRow = 0;

            // Título del reporte
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE DE CATEGORÍAS - " + reportDTO.getTitulo());
            titleCell.setCellStyle(titleStyle);

            // Información del reporte
            Row infoRow1 = sheet.createRow(currentRow++);
            Cell infoCell1 = infoRow1.createCell(0);
            infoCell1.setCellValue("Fecha de generación: " + reportDTO.getFechaGeneracion());
            infoCell1.setCellStyle(normalStyle);

            if (reportDTO.getNegocioNombre() != null) {
                Row infoRow2 = sheet.createRow(currentRow++);
                Cell infoCell2 = infoRow2.createCell(0);
                infoCell2.setCellValue("Negocio: " + reportDTO.getNegocioNombre());
                infoCell2.setCellStyle(normalStyle);
            }

            currentRow++; // Espacio en blanco

            if ("detallado".equalsIgnoreCase(tipoReporte)) {
                generateDetalladoExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle);
            } else {
                generateResumenExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle);
            }

            // Escribir el workbook al output stream
            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error generando reporte Excel de categorías", e);
            throw new RuntimeException("Error al generar el reporte Excel: " + e.getMessage());
        }
    }

    private void generateDetalladoExcelReport(Sheet sheet, CategoriaReportDTO reportDTO, int startRow,
                                              CellStyle headerStyle, CellStyle normalStyle) {
        // Configurar anchos de columnas para reporte detallado
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 6000);   // Nombre
        sheet.setColumnWidth(2, 8000);   // Descripción
        sheet.setColumnWidth(3, 4000);   // N° Productos
        sheet.setColumnWidth(4, 12000);  // Productos Principales

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "DESCRIPCIÓN", "N° PRODUCTOS", "PRODUCTOS PRINCIPALES"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de las categorías
        for (var categoria : reportDTO.getCategorias()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(categoria.getId());
            dataRow.createCell(1).setCellValue(categoria.getNombre());
            dataRow.createCell(2).setCellValue(categoria.getDescripcion());
            dataRow.createCell(3).setCellValue(categoria.getCantidadProductos());
            dataRow.createCell(4).setCellValue(categoria.getProductosPrincipales());

            // Aplicar estilo normal a todas las celdas
            for (int i = 0; i < headers.length; i++) {
                dataRow.getCell(i).setCellStyle(normalStyle);
            }
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()));
    }

    private void generateResumenExcelReport(Sheet sheet, CategoriaReportDTO reportDTO, int startRow,
                                            CellStyle headerStyle, CellStyle normalStyle) {
        // Configurar anchos de columnas para reporte resumen
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 8000);   // Nombre
        sheet.setColumnWidth(2, 4000);   // N° Productos
        sheet.setColumnWidth(3, 6000);   // Descripción

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "N° PRODUCTOS", "DESCRIPCIÓN"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de las categorías
        for (var categoria : reportDTO.getCategorias()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(categoria.getId());
            dataRow.createCell(1).setCellValue(categoria.getNombre());
            dataRow.createCell(2).setCellValue(categoria.getCantidadProductos());
            dataRow.createCell(3).setCellValue(categoria.getDescripcion());

            // Aplicar estilo normal a todas las celdas
            for (int i = 0; i < headers.length; i++) {
                dataRow.getCell(i).setCellStyle(normalStyle);
            }
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()));
    }

    private void addResumenExcelSection(Sheet sheet, CategoriaReportDTO reportDTO, int startRow,
                                        CellStyle normalStyle, CellStyle summaryStyle) {
        int currentRow = startRow;

        // Resumen
        Row summaryTitleRow = sheet.createRow(currentRow++);
        Cell summaryTitleCell = summaryTitleRow.createCell(0);
        summaryTitleCell.setCellValue("RESUMEN ESTADÍSTICO:");
        summaryTitleCell.setCellStyle(summaryStyle);

        Row summaryRow1 = sheet.createRow(currentRow++);
        Cell summaryCell1 = summaryRow1.createCell(0);
        summaryCell1.setCellValue("Total de categorías: " + reportDTO.getTotalCategorias());
        summaryCell1.setCellStyle(summaryStyle);

        Row summaryRow2 = sheet.createRow(currentRow++);
        Cell summaryCell2 = summaryRow2.createCell(0);
        summaryCell2.setCellValue("Total de productos: " + reportDTO.getTotalProductos());
        summaryCell2.setCellStyle(summaryStyle);

        if (reportDTO.getTotalCategorias() > 0) {
            double promedioProductos = (double) reportDTO.getTotalProductos() / reportDTO.getTotalCategorias();

            Row summaryRow3 = sheet.createRow(currentRow++);
            Cell summaryCell3 = summaryRow3.createCell(0);
            summaryCell3.setCellValue(String.format("Promedio de productos por categoría: %.1f", promedioProductos));
            summaryCell3.setCellStyle(summaryStyle);

            // Encontrar categorías con más y menos productos
            var categoriaConMasProductos = reportDTO.getCategorias().stream()
                    .max((c1, c2) -> Integer.compare(c1.getCantidadProductos(), c2.getCantidadProductos()));

            var categoriaConMenosProductos = reportDTO.getCategorias().stream()
                    .min((c1, c2) -> Integer.compare(c1.getCantidadProductos(), c2.getCantidadProductos()));

            if (categoriaConMasProductos.isPresent()) {
                Row summaryRow4 = sheet.createRow(currentRow++);
                Cell summaryCell4 = summaryRow4.createCell(0);
                summaryCell4.setCellValue("Categoría con más productos: " +
                        categoriaConMasProductos.get().getNombre() + " (" +
                        categoriaConMasProductos.get().getCantidadProductos() + " productos)");
                summaryCell4.setCellStyle(normalStyle);
            }

            if (categoriaConMenosProductos.isPresent()) {
                Row summaryRow5 = sheet.createRow(currentRow++);
                Cell summaryCell5 = summaryRow5.createCell(0);
                summaryCell5.setCellValue("Categoría con menos productos: " +
                        categoriaConMenosProductos.get().getNombre() + " (" +
                        categoriaConMenosProductos.get().getCantidadProductos() + " productos)");
                summaryCell5.setCellStyle(normalStyle);
            }
        }
    }

    private CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.DARK_GREEN.getIndex());

        style.setFont(font);
        return style;
    }

    public byte[] generateProductosExcelReport(ProductoReportDTO reportDTO, String tipoReporte) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Crear hoja de trabajo
            Sheet sheet = workbook.createSheet("Reporte de Productos");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            CellStyle summaryStyle = createSummaryStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle percentageStyle = createPercentageStyle(workbook);

            int currentRow = 0;

            // Título del reporte
            Row titleRow = sheet.createRow(currentRow++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("REPORTE DE PRODUCTOS - " + reportDTO.getTitulo());
            titleCell.setCellStyle(titleStyle);

            // Información del reporte
            Row infoRow1 = sheet.createRow(currentRow++);
            Cell infoCell1 = infoRow1.createCell(0);
            infoCell1.setCellValue("Fecha de generación: " + reportDTO.getFechaGeneracion());
            infoCell1.setCellStyle(normalStyle);

            if (reportDTO.getNegocioNombre() != null) {
                Row infoRow2 = sheet.createRow(currentRow++);
                Cell infoCell2 = infoRow2.createCell(0);
                infoCell2.setCellValue("Negocio: " + reportDTO.getNegocioNombre());
                infoCell2.setCellStyle(normalStyle);
            }

            currentRow++; // Espacio en blanco

            switch (tipoReporte.toLowerCase()) {
                case "inventario":
                    generateInventarioExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle, currencyStyle);
                    break;
                case "rentabilidad":
                    generateRentabilidadExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle, currencyStyle, percentageStyle);
                    break;
                case "resumen":
                    generateResumenProductosExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle);
                    break;
                default:
                    generateDefaultProductosExcelReport(sheet, reportDTO, currentRow, headerStyle, normalStyle, currencyStyle);
                    break;
            }

            // Escribir el workbook al output stream
            workbook.write(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            log.error("Error generando reporte Excel de productos", e);
            throw new RuntimeException("Error al generar el reporte Excel: " + e.getMessage());
        }
    }

    private void generateInventarioExcelReport(Sheet sheet, ProductoReportDTO reportDTO, int startRow,
                                               CellStyle headerStyle, CellStyle normalStyle, CellStyle currencyStyle) {
        // Configurar anchos de columnas para reporte de inventario
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 8000);   // Nombre
        sheet.setColumnWidth(2, 4000);   // Precio
        sheet.setColumnWidth(3, 4000);   // Costo
        sheet.setColumnWidth(4, 4000);   // Margen
        sheet.setColumnWidth(5, 8000);   // Categorías

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "PRECIO", "COSTO", "MARGEN", "CATEGORÍAS"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de los productos
        for (var producto : reportDTO.getProductos()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(producto.getId());
            dataRow.createCell(1).setCellValue(producto.getNombre());

            Cell precioCell = dataRow.createCell(2);
            precioCell.setCellValue(producto.getPrecioUnitario());
            precioCell.setCellStyle(currencyStyle);

            Cell costoCell = dataRow.createCell(3);
            costoCell.setCellValue(producto.getCosto());
            costoCell.setCellStyle(currencyStyle);

            Cell margenCell = dataRow.createCell(4);
            margenCell.setCellValue(producto.getMargenGanancia());
            margenCell.setCellStyle(currencyStyle);

            dataRow.createCell(5).setCellValue(producto.getCategorias());

            // Aplicar estilo normal a las celdas de texto
            dataRow.getCell(0).setCellStyle(normalStyle);
            dataRow.getCell(1).setCellStyle(normalStyle);
            dataRow.getCell(5).setCellStyle(normalStyle);
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenProductosExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()), currencyStyle);
    }

    private void generateRentabilidadExcelReport(Sheet sheet, ProductoReportDTO reportDTO, int startRow,
                                                 CellStyle headerStyle, CellStyle normalStyle,
                                                 CellStyle currencyStyle, CellStyle percentageStyle) {
        // Configurar anchos de columnas para reporte de rentabilidad
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 8000);   // Nombre
        sheet.setColumnWidth(2, 4000);   // Precio
        sheet.setColumnWidth(3, 4000);   // Costo
        sheet.setColumnWidth(4, 4000);   // Margen $
        sheet.setColumnWidth(5, 4000);   // Margen %
        sheet.setColumnWidth(6, 6000);   // Categorías

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "PRECIO", "COSTO", "MARGEN $", "MARGEN %", "CATEGORÍAS"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de los productos
        for (var producto : reportDTO.getProductos()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(producto.getId());
            dataRow.createCell(1).setCellValue(producto.getNombre());

            Cell precioCell = dataRow.createCell(2);
            precioCell.setCellValue(producto.getPrecioUnitario());
            precioCell.setCellStyle(currencyStyle);

            Cell costoCell = dataRow.createCell(3);
            costoCell.setCellValue(producto.getCosto());
            costoCell.setCellStyle(currencyStyle);

            Cell margenCell = dataRow.createCell(4);
            margenCell.setCellValue(producto.getMargenGanancia());
            margenCell.setCellStyle(currencyStyle);

            Cell porcentajeCell = dataRow.createCell(5);
            porcentajeCell.setCellValue(producto.getPorcentajeMargen() / 100); // Convertir a decimal para formato porcentaje
            porcentajeCell.setCellStyle(percentageStyle);

            dataRow.createCell(6).setCellValue(producto.getCategorias());

            // Aplicar estilo normal a las celdas de texto
            dataRow.getCell(0).setCellStyle(normalStyle);
            dataRow.getCell(1).setCellStyle(normalStyle);
            dataRow.getCell(6).setCellStyle(normalStyle);
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenProductosExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()), currencyStyle);
    }

    private void generateResumenProductosExcelReport(Sheet sheet, ProductoReportDTO reportDTO, int startRow,
                                                     CellStyle headerStyle, CellStyle normalStyle) {
        // Configurar anchos de columnas para reporte resumen
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 8000);   // Nombre
        sheet.setColumnWidth(2, 4000);   // Precio
        sheet.setColumnWidth(3, 4000);   // Categorías

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "PRECIO", "CATEGORÍAS"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de los productos
        for (var producto : reportDTO.getProductos()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(producto.getId());
            dataRow.createCell(1).setCellValue(producto.getNombre());
            dataRow.createCell(2).setCellValue(producto.getPrecioUnitario());
            dataRow.createCell(3).setCellValue(producto.getCantidadCategorias());

            // Aplicar estilo normal a todas las celdas
            for (int i = 0; i < headers.length; i++) {
                dataRow.getCell(i).setCellStyle(normalStyle);
            }
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenProductosExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()), createCurrencyStyle(sheet.getWorkbook()));
    }

    private void generateDefaultProductosExcelReport(Sheet sheet, ProductoReportDTO reportDTO, int startRow,
                                                     CellStyle headerStyle, CellStyle normalStyle, CellStyle currencyStyle) {
        // Configurar anchos de columnas para reporte por defecto
        sheet.setColumnWidth(0, 2000);   // ID
        sheet.setColumnWidth(1, 8000);   // Nombre
        sheet.setColumnWidth(2, 4000);   // Precio
        sheet.setColumnWidth(3, 4000);   // Costo
        sheet.setColumnWidth(4, 8000);   // Categorías

        int currentRow = startRow;

        // Encabezados de la tabla
        Row headerRow = sheet.createRow(currentRow++);
        String[] headers = {"ID", "NOMBRE", "PRECIO", "COSTO", "CATEGORÍAS"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos de los productos
        for (var producto : reportDTO.getProductos()) {
            Row dataRow = sheet.createRow(currentRow++);

            dataRow.createCell(0).setCellValue(producto.getId());
            dataRow.createCell(1).setCellValue(producto.getNombre());

            Cell precioCell = dataRow.createCell(2);
            precioCell.setCellValue(producto.getPrecioUnitario());
            precioCell.setCellStyle(currencyStyle);

            Cell costoCell = dataRow.createCell(3);
            costoCell.setCellValue(producto.getCosto());
            costoCell.setCellStyle(currencyStyle);

            dataRow.createCell(4).setCellValue(producto.getCategorias());

            // Aplicar estilo normal a las celdas de texto
            dataRow.getCell(0).setCellStyle(normalStyle);
            dataRow.getCell(1).setCellStyle(normalStyle);
            dataRow.getCell(4).setCellStyle(normalStyle);
        }

        currentRow++; // Espacio en blanco

        // Agregar resumen
        addResumenProductosExcelSection(sheet, reportDTO, currentRow, normalStyle, createSummaryStyle(sheet.getWorkbook()), currencyStyle);
    }

    private void addResumenProductosExcelSection(Sheet sheet, ProductoReportDTO reportDTO, int startRow,
                                                 CellStyle normalStyle, CellStyle summaryStyle, CellStyle currencyStyle) {
        int currentRow = startRow;

        // Resumen
        Row summaryTitleRow = sheet.createRow(currentRow++);
        Cell summaryTitleCell = summaryTitleRow.createCell(0);
        summaryTitleCell.setCellValue("RESUMEN ESTADÍSTICO:");
        summaryTitleCell.setCellStyle(summaryStyle);

        Row summaryRow1 = sheet.createRow(currentRow++);
        Cell summaryCell1 = summaryRow1.createCell(0);
        summaryCell1.setCellValue("Total de productos: " + reportDTO.getTotalProductos());
        summaryCell1.setCellStyle(summaryStyle);

        Row summaryRow2 = sheet.createRow(currentRow++);
        Cell summaryCell2 = summaryRow2.createCell(0);
        summaryCell2.setCellValue("Valor total del inventario: ");
        summaryCell2.setCellStyle(summaryStyle);

        Cell valorCell = summaryRow2.createCell(1);
        valorCell.setCellValue(reportDTO.getValorTotalInventario());
        valorCell.setCellStyle(currencyStyle);

        Row summaryRow3 = sheet.createRow(currentRow++);
        Cell summaryCell3 = summaryRow3.createCell(0);
        summaryCell3.setCellValue("Precio promedio: ");
        summaryCell3.setCellStyle(summaryStyle);

        Cell precioPromedioCell = summaryRow3.createCell(1);
        precioPromedioCell.setCellValue(reportDTO.getPrecioPromedio());
        precioPromedioCell.setCellStyle(currencyStyle);

        if (reportDTO.getTotalProductos() > 0) {
            double promedioCategorias = (double) reportDTO.getProductos().stream()
                    .mapToInt(ProductoReportDataDTO::getCantidadCategorias)
                    .sum() / reportDTO.getTotalProductos();

            Row summaryRow4 = sheet.createRow(currentRow++);
            Cell summaryCell4 = summaryRow4.createCell(0);
            summaryCell4.setCellValue(String.format("Promedio de categorías por producto: %.1f", promedioCategorias));
            summaryCell4.setCellStyle(summaryStyle);

            // Encontrar productos con mayor y menor margen
            var productoMayorMargen = reportDTO.getProductos().stream()
                    .max((p1, p2) -> Double.compare(p1.getPorcentajeMargen(), p2.getPorcentajeMargen()));

            var productoMenorMargen = reportDTO.getProductos().stream()
                    .min((p1, p2) -> Double.compare(p1.getPorcentajeMargen(), p2.getPorcentajeMargen()));

            if (productoMayorMargen.isPresent()) {
                Row summaryRow5 = sheet.createRow(currentRow++);
                Cell summaryCell5 = summaryRow5.createCell(0);
                summaryCell5.setCellValue("Producto con mayor margen: " +
                        productoMayorMargen.get().getNombre() + " (" +
                        String.format("%.1f%%", productoMayorMargen.get().getPorcentajeMargen()) + ")");
                summaryCell5.setCellStyle(normalStyle);
            }

            if (productoMenorMargen.isPresent()) {
                Row summaryRow6 = sheet.createRow(currentRow++);
                Cell summaryCell6 = summaryRow6.createCell(0);
                summaryCell6.setCellValue("Producto con menor margen: " +
                        productoMenorMargen.get().getNombre() + " (" +
                        String.format("%.1f%%", productoMenorMargen.get().getPorcentajeMargen()) + ")");
                summaryCell6.setCellStyle(normalStyle);
            }
        }
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createPercentageStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("0.0%"));
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
}
