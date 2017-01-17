package com.cosmos.utils.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	
	public static interface MD5Progress{
		public void progress(double value);
	}
	
	public static String stringToMd5By32Bit(String plainText)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plainText.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuilder buf = new StringBuilder("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	public static String stringToMd5By16Bit(String plainText)
			throws NoSuchAlgorithmException {
		return stringToMd5By32Bit(plainText).substring(8, 24);
	}

	public static String fileToMd5(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			while((index = in.read(buffer, 0, bufferSize)) > 0){
				md5.update(buffer,0,index);
			}
		} catch (IOException e) {
			throw e;
		} finally{
			in.close();
		}
		BigInteger bi = new BigInteger(1, md5.digest());
		return bi.toString(16);
	}
	
	public static String fileToMd5(File file,MD5Progress progress) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		FileInputStream in = new FileInputStream(file);
		long length = file.length();
		long current = 0L;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int index = -1;
		try {
			while((index = in.read(buffer, 0, bufferSize)) > 0){
				md5.update(buffer,0,index);
				current += index;
				progress.progress(current * 100.0 / length); 
			}
		} catch (IOException e) {
			throw e;
		} finally{
			in.close();
		}
		BigInteger bi = new BigInteger(1, md5.digest());
		return bi.toString(16);
	}

}
