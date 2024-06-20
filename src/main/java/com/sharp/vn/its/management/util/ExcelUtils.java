package com.sharp.vn.its.management.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Excel utils.
 */
public class ExcelUtils {


    /**
     * Create workbook workbook.
     *
     * @return the workbook
     */
    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    /**
     * Create sheet sheet.
     *
     * @param workbook the workbook
     * @param sheetName the sheet name
     * @return the sheet
     */
    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * Add header row.
     *
     * @param sheet the sheet
     * @param headers the headers
     */
    public static void addHeaderRow(Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }
    }

    /**
     * Format cell.
     *
     * @param cell the cell
     * @param style the style
     */
    public static void formatCell(Cell cell, CellStyle style) {
        cell.setCellStyle(style);
    }

    /**
     * Write cell.
     *
     * @param row the row
     * @param colIndex the col index
     * @param value the value
     */
    public static void writeCell(Row row, int colIndex, Object value) {
        Cell cell = row.createCell(colIndex);
        if (value == null) {
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double || value instanceof Integer || value instanceof Long
                || value instanceof Float) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof LocalDateTime dateTime) {
            cell.setCellValue(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateTime));
        } else {
            throw new IllegalArgumentException(
                    "Unsupported cell value type: " + value.getClass().getName());
        }
    }

    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
