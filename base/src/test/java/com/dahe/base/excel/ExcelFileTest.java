package com.dahe.base.excel;

import java.io.File;
import java.io.IOException;
//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.dahe.base.excel.service.IExcelFileExportor;
import com.dahe.base.excel.service.impl.Excel97FileExportor;

public class ExcelFileTest {

	public static HttpServletResponse response;
	public static String fileName = "sheet";
	
	public static void main(String[] args) {
		IExcelFileExportor fileExportor = new Excel97FileExportor(fileName);
		
		/*try {
			if (fileExportor != null) {
				List<String> head = new ArrayList<String>();
				head.add("id");
				head.add("name");
				head.add("wage");
				fileExportor.appendRow(head);
				
				response.setContentType("application/msexcel;charset=utf-8");
				response.setHeader("content-disposition", "attachment;filename=\""+URLEncoder.encode(fileName, "utf-8")+".xls\"");
				OutputStream out = response.getOutputStream();
				fileExportor.write(out);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		try {
			if (fileExportor != null) {
				List<String> head = new ArrayList<String>();
				head.add("id");
				head.add("name");
				head.add("wage");
				fileExportor.appendRow(head);
				
				for (int i = 0; i < 70000; i++) {
					List<String> row = new ArrayList<String>();
					row.add("1");
					row.add("john");
					row.add("100000");
					fileExportor.appendRow(row);
				}
				
				
				File file = new File("D:\\file\\sheet.xls");
				fileExportor.write(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
