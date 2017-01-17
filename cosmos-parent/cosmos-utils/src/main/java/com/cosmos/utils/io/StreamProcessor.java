package com.cosmos.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;

import com.cosmos.utils.io.exception.SignedTypeFormatException;

public class StreamProcessor {
	
	public static interface Progress{
		public void init(double current,double max);
		public void onProgress(double val);
	}
	
	public static byte[] readLine(InputStream input) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(64);
		int bty = -1;
		while((bty = input.read())!= -1){
			if(bty != '\r' && bty !='\n'){
				out.write(bty);
			} else if(bty == '\n'){
				break;
			} else {
				bty = input.read();
				if(bty == '\n'){
					break;
				} else {
					out.write('\r');
					out.write(bty);
				}
			} 
		}
		return out.toByteArray();
	}
	
	public static String readLine(InputStream input,String charset) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(64);
		int bty = -1;
		while((bty = input.read())!= -1){
			if(bty != '\r' && bty !='\n'){
				out.write(bty);
			} else if(bty == '\r'){
				bty = input.read();
				if(bty == '\n'){
					break;
				} else {
					out.write('\r');
					out.write(bty);
				}
			} else {
				break;
			}
		}
		return new String(out.toByteArray(),charset);
	}
	
	public static byte[] readByte(InputStream input) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamProcessor.forword(input, out);
		byte[] result = out.toByteArray();
		out = null;
		return result;
	}
	
	public static byte[] readByte(InputStream input,long length) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(50);
		StreamProcessor.forword(input, out, length);
		byte[] result = out.toByteArray();
		out = null;
		return result;
	}
	
	public static void forword(InputStream input,OutputStream out,Progress progress) throws IOException{
		int index = -1;
		int bufferSize = 1024;
		double readBytes = 0;
		byte[] buffer = new byte[bufferSize];
		while((index = input.read(buffer, 0, bufferSize)) != -1){
			out.write(buffer, 0, index);
			if(progress != null){
				readBytes+=index;
				progress.onProgress(readBytes);
			}
		}
		buffer = null;
	}
	
	public static void forword(InputStream input,OutputStream out) throws IOException{
		int index = -1;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		while((index = input.read(buffer, 0, bufferSize)) != -1){
			out.write(buffer, 0, index);
		}
		buffer = null;
	}
	
	
	
	public static void forword(InputStream input,OutputStream out,long length,Progress progress) throws IOException{
		int index = -1;
		long readBytes = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		do{
			if(length - readBytes < bufferSize){
				bufferSize = (int) (length - readBytes);
			}
			index = input.read(buffer, 0, bufferSize);
			readBytes += index;
			out.write(buffer,0,index);
			if(progress != null){
				progress.onProgress(readBytes);
			}
		}
		while(index != -1 && readBytes < length);
		buffer = null;
	}
	
	public static void forword(InputStream input,OutputStream out,long length) throws IOException{
		int index = -1;
		long readBytes = 0;
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		do{
			if(length - readBytes < bufferSize){
				bufferSize = (int) (length - readBytes);
			}
			index = input.read(buffer, 0, bufferSize);
			readBytes += index;
			out.write(buffer,0,index);
		}
		while(index != -1 && readBytes < length);
		buffer = null;
	}
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	private ByteOrder order;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	public ByteOrder getOrder() {
		return order;
	}

	public void setOrder(ByteOrder order) {
		this.order = order;
	}
	
	public StreamProcessor(InputStream inputStream,OutputStream outputStream){
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.order = ByteOrder.BIG_ENDIAN;
	}
	
	public void close() throws IOException{
		if(this.inputStream!=null){
			this.inputStream.close();
		}
		if(this.outputStream!=null){
			this.outputStream.close();
		}
	}
	
	public byte[] readBytes(long length) throws IOException{
		return StreamProcessor.readByte(this.inputStream, length);
	}
	
	public short readInt16() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,2);
		return SignedDataTypeConverter.toInt16(data, this.order);
	}
	
	public int readInt32() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,4);
		return SignedDataTypeConverter.toInt32(data, this.order);
	}

	public long readInt64() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,8);
		return SignedDataTypeConverter.toInt64(data, this.order);
	}
	
	public float readFloat() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,4);
		return SignedDataTypeConverter.toFloat(data, this.order);
	}

	public double readDouble() throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,8);
		return SignedDataTypeConverter.toDouble(data, this.order);
	}
	
	public String readString(int length,String charset) throws IOException, SignedTypeFormatException{
		byte[] data = StreamProcessor.readByte(this.inputStream,length);
		return new String(data,charset);
	}
	
	public void readInputStream(OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out);
	}
	
	public void readInputStream(OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,progress);
	}
	
	public void readInputStream(long length,OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length);
	}
	
	public void readInputStream(long length,OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length,progress);
	}
	
	public void writeBytes(byte...number) throws IOException{
		this.outputStream.write(number);
	}
	
	public void writeInt16(short number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}
	
	public void writeInt32(int number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}
	
	public void writeInt64(long number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}
	
	public void writeFloat(float number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}
	
	public void writeDouble(double number) throws IOException{
		this.outputStream.write(SignedDataTypeConverter.toByte(number, this.order));
	}
	
	public void writeString(String str,String charset) throws IOException{
		byte[] bytes = str.getBytes(charset);
		this.outputStream.write(bytes);
	}
	
	public void writeFromInputStream(InputStream in) throws IOException{
		StreamProcessor.forword(in, this.outputStream);
	}
	
	public void writeFromInputStream(InputStream in,Progress progress) throws IOException{
		StreamProcessor.forword(in, this.outputStream,progress);
	}
	
	public void writeFromInputStream(InputStream in,long length) throws IOException{
		StreamProcessor.forword(in, this.outputStream, length);
	}
	
	public void writeFromInputStream(InputStream in,long length,Progress progress) throws IOException{
		StreamProcessor.forword(in, this.outputStream,length,progress);
	}
	
	
	public void writeToOutputStream(OutputStream out) throws IOException{
		StreamProcessor.forword(this.inputStream, out);
	}
	
	public void writeToOutputStream(OutputStream out,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,progress);
	}
	
	public void writeToOutputStream(OutputStream out,long length) throws IOException{
		StreamProcessor.forword(this.inputStream, out, length);
	}
	
	public void writeToOutputStream(OutputStream out,long length,Progress progress) throws IOException{
		StreamProcessor.forword(this.inputStream, out,length,progress);
	}
}
