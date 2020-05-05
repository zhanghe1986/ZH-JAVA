package com.dahe.base.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.springframework.util.FileCopyUtils;

public class FileUtil {

	/**
	 * @describe copy file
	 * */
	public static void copyFile(String source, String destination)
			throws FileNotFoundException, IOException {
		if (source != null && source.length() > 0 && destination != null
				&& destination.length() > 0) {
			FileCopyUtils.copy(new File(source), new File(destination));
		}
	}

	/**
	 * @describe copy file
	 * */
	public static void copyFile(File source, File destination)
			throws IOException {
		FileCopyUtils.copy(source, destination);
	}

	/**
	 * @describe copy file
	 * */
	public static void copyFile(InputStream is, OutputStream os)
			throws IOException {
		FileCopyUtils.copy(is, os);
	}

	/**
	 * @describe rename file
	 * */
	public static void renameFile(String filePath, String oldFileName,
			String newFileName) {
		if (filePath != null && filePath.length() > 0 && oldFileName != null
				&& oldFileName.length() > 0 && newFileName != null
				&& newFileName.length() > 0) {
			renameFile(new File(filePath + oldFileName), new File(filePath
					+ newFileName));
		}
	}
	
	/**
	 * @describe rename file
	 * */
	private static void renameFile(File oldFile, File newFile) {
		if (oldFile != null && newFile != null) {
			if (newFile.exists()) {
				System.out.println("the new file name is exist.");
			} else {
				oldFile.renameTo(newFile);
			}
		}
	}
	
	/**
	 * @describe delete file
	 * */
	public static void deleteFile(String filePath, String fileName) {
		deleteFile(new File(filePath+fileName));
	}
	
	/**
	 * @describe delete file
	 * */
	private static void deleteFile(File file) {
		if (file != null) {
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}
	
	/**
	 * @describe wirte file
	 * */
	public static void writeFile(String filePath, byte[] bytes) {
		int bufferSize = 1024;
		FileOutputStream fileOutputStream = null;
		
		try {
			File file = new File(filePath);
			fileOutputStream = new FileOutputStream(file);
			int length = bytes.length;
			int start = 0;
			while (length > start+bufferSize) {
				fileOutputStream.write(bytes, start, bufferSize);
				start = start + bufferSize;
			}
			if (length != start+bufferSize) {
				bufferSize = length - start;
				fileOutputStream.write(bytes, start, bufferSize);
			} else {
				fileOutputStream.write(bytes, start, bufferSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @describe recursive to delete all files and directories of the directory
	 * */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (children != null) {
				for (String child : children) {
					boolean sucess = deleteDir(new File(dir, child));
					if (!sucess) {
						return false;
					}
				}
			}
		}
		return dir.delete();
	}
	
	/**
	 * @describe create directory
	 * */
	public static File createDir(String dirPath) {
		File dirFile = null;
		try {
			dirFile = new File(dirPath);
			if (!dirFile.exists() && !dirFile.isDirectory()) {
				dirFile.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dirFile;
	}
	
	/**
	 * @describe append content in the file
	 * */
	public static void insertFileTail(String fileName, String content) {
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			copyFile("e:/1.txt", "e:/2.txt");
			renameFile("e:/", "2.txt", "3.txt");
			deleteFile("e:/", "3.txt");
			File f1 = new File("g:/1.zip");
			File f2 = new File("g:/2.zip");
			copyFile(new FileInputStream(f1), new FileOutputStream(f2));
			File file = new File("e:/picture");
			deleteDir(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
