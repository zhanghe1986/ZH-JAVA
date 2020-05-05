package com.dahe.base.excel.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IExcelFileExportor {

	public void appendRow(List<String> row);
	public void write(OutputStream out) throws IOException;
	public void write(File file) throws IOException;
	
}
