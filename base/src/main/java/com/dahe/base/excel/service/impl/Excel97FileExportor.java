package com.dahe.base.excel.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.dahe.base.excel.service.IExcelFileExportor;

public class Excel97FileExportor implements IExcelFileExportor {

	private static final int MAX_ROW = 65535;
	private static final short MAX_COLUMN = (short) 0x00FF;
	private HSSFWorkbook wb;
	private Sheet sheet;
	private CellStyle cellStyle;
	private int columnNo;
	private int rowIndex = 0;
	private int sheetIndex = 0;
	private String sheetName = "sheet";
	
	public Excel97FileExportor(String sheetName) {
		this.sheetName = sheetName;
		init();
	}
	
	public Excel97FileExportor() {
		init();
	}
	
	private void init() {
		wb = new HSSFWorkbook();
		cellStyle = wb.createCellStyle();
		createSheet();
		Font font = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "songti", false,
				(short) 200);
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
		cellStyle.setFont(font);
	}
	
	private void createSheet() {
		if (this.sheetIndex == 0) {
			this.sheet = wb.createSheet(this.sheetName);
		} else {
			this.sheet = wb.createSheet(this.sheetName+"_"+this.sheetIndex);
		}
		this.sheet.setDefaultColumnWidth(20);
		this.sheetIndex++;
	}
	
	@Override
	public void appendRow(List<String> row) {
		if (row == null || row.isEmpty()) {
			return;
		}
		
		if (row.size() > MAX_COLUMN) {
			throw new IllegalArgumentException("big row");
		}
		
		if (this.rowIndex > MAX_ROW) {
			createSheet();
			this.rowIndex = 0;
		}
		
		Row rowData = sheet.createRow(this.rowIndex++);
		this.columnNo = 0;
		for (String cell : row) {
			createCell(rowData, this.columnNo++, cell);
		}
	}
	
	private void createCell(Row row, int column, String value) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellStyle(this.cellStyle);
	}
	
	private Font createFonts(Workbook wb, short bold, String fontName,
			boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		wb.write(out);
	}
	
	@Override
	public void write(File file) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
