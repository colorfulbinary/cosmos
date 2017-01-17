package com.cosmos.utils.network.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

import com.cosmos.utils.io.StreamProcessor;

public class HttpUtils {
	public static Map<String, String> parseHttpUploadMessage(InputStream source,Map<String, OutputStream> output,String charset) throws IOException{
		PushbackInputStream input = new PushbackInputStream(source);
		HashMap<String, String> result = new HashMap<String, String>();
		String line =  StreamProcessor.readLine(input, "iso-8859-1");
		String splitChars = line;
		while(line!=null){
			String name = null;
			boolean isFile = false;
			while((line = StreamProcessor.readLine(input, "iso-8859-1")) != null && !line.trim().equals("")){
				if(line.startsWith("Content-Disposition")){
					String[] str = line.substring(line.indexOf(':')+1).split(";");
					for (String item : str) {
						if(item.trim().startsWith("name")){
							name = item.substring(item.indexOf('=') + 2,item.lastIndexOf('"'));
						} else if(item.trim().startsWith("filename")){
							isFile = true;
						}
					}
				}
			}
			OutputStream out = null;
			if(isFile && output.containsKey(name)){
				out = output.get(name);
			} else {
				out = new ByteArrayOutputStream(128);
			}
			int bufferSize = splitChars.length();
			byte[] buffer = new byte[bufferSize];
			int bty = -1;
			String ln = null;
			while((bty = input.read())!=-1){
				if(bty == '\n'){
					int index = 0;
					while(index < bufferSize){
						bty = input.read();
						if(bty == '\n'){
							break;
						}else {
							buffer[index++] = (byte)bty;
						}
					}
					byte[] strBuffer = new byte[index];
					System.arraycopy(buffer, 0, strBuffer, 0, index);
					ln = new String(strBuffer);
					if(ln.startsWith(splitChars)){
						break;
					} else {
						out.write('\n');
						out.write(strBuffer);
						if(index != bufferSize){
							input.unread('\n');
						}
					}
				} else {
					out.write(bty);
				}
			}
			out.close();
			if(!isFile || !output.containsKey(name)){
				String value = new String(((ByteArrayOutputStream)out).toByteArray(),charset);
				result.put(name, value);
			}
			if((line = StreamProcessor.readLine(input, "iso-8859-1")).startsWith("--")){
				break;
			}
			
		}
		return result;
	}
}
