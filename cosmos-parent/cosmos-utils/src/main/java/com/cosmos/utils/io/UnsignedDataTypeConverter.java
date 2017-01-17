package com.cosmos.utils.io;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.cosmos.utils.io.exception.UnsignedTypeFormatException;

/*
 * ClassName : UnsignedDataTypeConverter
 * Creator   : rouies
 * Function  : 实现java下的无符号整数与字节之间的转换
 * Date      : 2015-8-15
 */
public class UnsignedDataTypeConverter {
	//默认字节序列
	private static ThreadLocal<ByteOrder> v_order = new ThreadLocal<ByteOrder>(){
		protected ByteOrder initialValue() {
			return ByteOrder.LITTLE_ENDIAN;
		};
	};
	
	
	/*
	 * MethodName : setByteOrder
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 设置转换时默认使用的字节序列
	 * Arguments  : ByteOrder order -> 要设置的默认字节序
	 * Return     : void
	 */
	public static void setDefaultByteOrder(ByteOrder order){
		UnsignedDataTypeConverter.v_order.set(order);
	}
	
	
	/*
	 * MethodName : convertUnsignedTypeToBigInteger
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将无符号整数字节序列转换为有符号的整数
	 * Arguments  : byte...number -> 要转换的字节序列
	 * Return     : 转换后的BigInteger
	 */
	public static BigInteger convertUnsignedTypeToBigInteger(byte...number) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.convertUnsignedTypeToBigInteger(v_order.get(),number);
	}
	
	/*
	 * MethodName : convertUInt32ToInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，将无符号32位整数(4 byte)转换为有符号的64位整数
	 * Arguments  : byte...ui32 -> 要转换的字节序列(必须是 4 byte)
	 * Return     : 转换后的long
	 */
	public static long convertUInt32ToInt64(byte...ui32) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.convertUInt32ToInt64(UnsignedDataTypeConverter.v_order.get(),ui32);
	}
	
	
	/*
	 * MethodName : convertUInt16ToInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，将无符号16位整数(2 byte)转换为有符号的32位整数
	 * Arguments  : byte...ui16 -> 要转换的字节序列(必须是 2 byte)
	 * Return     : 转换后的int
	 */
	public static int convertUInt16ToInt32(byte...ui16) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.convertUInt16ToInt32(UnsignedDataTypeConverter.v_order.get(),ui16);
	}
	
	/*
	 * MethodName : convertUInt8ToInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，将无符号8位整数(1 byte)转换为有符号的16位整数
	 * Arguments  : byte ui18 -> 要转换的字节序列
	 * Return     : 转换后的short
	 */
	public static short convertUInt8ToInt16(byte ui8){
		return UnsignedDataTypeConverter.convertUInt8ToInt16(UnsignedDataTypeConverter.v_order.get(),ui8);
	}
	
	
	/*
	 * MethodName : convertUnsignedTypeToBigInteger
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将无符号整数字节序列转换为有符号的整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte...number -> 要转换的字节序列
	 * Return     : 转换后的BigInteger
	 */
	public static BigInteger convertUnsignedTypeToBigInteger(ByteOrder order,byte...number) throws UnsignedTypeFormatException{
		if(number.length == 0){
			throw new UnsignedTypeFormatException("必须包含一个要转换的字节序列");
		}
		byte[] data = new byte[number.length + 1];
		data[0] = 0;
		if(order == ByteOrder.LITTLE_ENDIAN){
			ArraysUtils.reverseArray(number);
		}
		System.arraycopy(number, 0, data, 1, number.length);
		BigInteger result = new BigInteger(data);
		return result;
	}
	

	
	/*
	 * MethodName : convertUInt32ToInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定字节序，将无符号32位整数(int 4 byte)转换为有符号的64位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte...ui32 -> 要转换的字节序列(必须是 4 byte)
	 * Return     : 转换后的long
	 */
	public static long convertUInt32ToInt64(ByteOrder order,byte...ui32) throws UnsignedTypeFormatException{
		if(ui32.length > 4){
			throw new UnsignedTypeFormatException("要转换的类型必须是32位(4字节)");
		}
		BigInteger bi = UnsignedDataTypeConverter.convertUnsignedTypeToBigInteger(order, ui32);
		long result = bi.longValue();
		return result;
	}
	
	/*
	 * MethodName : convertUInt16ToInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定字节序，将无符号16位整数(short 2 byte)转换为有符号的32位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte...ui16 -> 要转换的字节序列(必须是 2 byte)
	 * Return     : 转换后的int
	 */
	public static int convertUInt16ToInt32(ByteOrder order,byte...ui16) throws UnsignedTypeFormatException{
		if(ui16.length > 2){
			throw new UnsignedTypeFormatException("要转换的类型必须是16位(2字节)");
		}
		BigInteger bi = UnsignedDataTypeConverter.convertUnsignedTypeToBigInteger(order, ui16);
		int result = bi.intValue();
		return result;
	}
	
	/*
	 * MethodName : convertUInt8ToInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将无符号8位整数(1 byte)转换为有符号的16位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte ui18 -> 要转换的字节序列
	 * Return     : 转换后的short
	 */
	public static short convertUInt8ToInt16(ByteOrder order,byte ui8){
		byte[] data = new byte[2];
		if (order == ByteOrder.LITTLE_ENDIAN) {
			data[0] = ui8;
		} else {
			data[1] = ui8;
		}
		ByteBuffer buffer = ByteBuffer.wrap(data);
		short result = buffer.order(order).getShort();
		return result;
	}
	
	
	/*
	 * MethodName : toUInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号整数转换为无符号的64位整数
	 * Arguments  : BigInteger number -> 要转换的整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt32(BigInteger number) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.toUInt64(UnsignedDataTypeConverter.v_order.get(),number);
	}
	
	
	
	/*
	 * MethodName : toUInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号64位整数(8 byte)转换为无符号的32位整数
	 * Arguments  : long number -> 要转换的64位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt32(long number) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.toUInt32(UnsignedDataTypeConverter.v_order.get(),number);
	}
	
	/*
	 * MethodName : toUInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号32位整数(4 byte)转换为无符号的16位整数
	 * Arguments  : int number -> 要转换的32位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt16(int number) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.toUInt16(UnsignedDataTypeConverter.v_order.get(),number);
	}
	
	/*
	 * MethodName : toUInt8
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将有符号16位整数(2 byte)转换为无符号的8位整数
	 * Arguments  : short number -> 要转换的16位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte toUInt8(short number) throws UnsignedTypeFormatException{
		return UnsignedDataTypeConverter.toUInt8(UnsignedDataTypeConverter.v_order.get(),number);
	}
	
	/*
	 * MethodName : toUInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号64位整数(8 byte)转换为无符号的32位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				long number -> 要转换的64位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt64(ByteOrder order,BigInteger number) throws UnsignedTypeFormatException{
		if(number.compareTo(
				BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2))
					.add(BigInteger.valueOf(1))) > 0 || number.compareTo(BigInteger.valueOf(0)) < 0) {
			throw new UnsignedTypeFormatException("无符号类型数值不匹配");
		}
		byte[] data = number.toByteArray();
		byte[] dest = new byte[8];
		System.arraycopy(data, 0, dest, 8 - data.length, data.length);
		if (order == ByteOrder.LITTLE_ENDIAN) {
			ArraysUtils.reverseArray(dest);
		}
		return dest;
	}
	
	
	/*
	 * MethodName : toUInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号64位整数(8 byte)转换为无符号的32位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				long number -> 要转换的64位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt32(ByteOrder order,long number) throws UnsignedTypeFormatException{
		if(number > Integer.MAX_VALUE * 2 + 1 || number < 0){
			throw new UnsignedTypeFormatException("无符号类型数值不匹配");
		}
		BigInteger bi = BigInteger.valueOf(number);
		byte[] data = bi.toByteArray();
		byte[] dest = new byte[4];
		System.arraycopy(data, 0, dest, 4 - data.length, data.length);
		if (order == ByteOrder.LITTLE_ENDIAN) {
			ArraysUtils.reverseArray(dest);
		}
		return dest;
	}
	
	/*
	 * MethodName : toUInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号32位整数(4 byte)转换为无符号的16位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				int number -> 要转换的32位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte[] toUInt16(ByteOrder order,int number) throws UnsignedTypeFormatException{
		if(number > Short.MAX_VALUE * 2 + 1 || number < 0){
			throw new UnsignedTypeFormatException("无符号类型数值不匹配");
		}
		BigInteger bi = BigInteger.valueOf(number);
		byte[] data = bi.toByteArray();
		byte[] dest = new byte[2];
		System.arraycopy(data, 0, dest, 2 - data.length, data.length);
		if (order == ByteOrder.LITTLE_ENDIAN) {
			ArraysUtils.reverseArray(dest);
		}
		return dest;
	}
	
	/*
	 * MethodName : toUInt8
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将有符号16位整数(2 byte)转换为无符号的8位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				short number -> 要转换的16位整数
	 * Return     : 转换后的字节序列
	 */
	public static byte toUInt8(ByteOrder order,short number) throws UnsignedTypeFormatException{
		if(number > Byte.MAX_VALUE * 2 + 1 || number < 0){
			throw new UnsignedTypeFormatException("无符号类型数值不匹配");
		}
		ByteBuffer buffer = ByteBuffer.allocate(2).order(order).putShort(number);
		byte[] data = buffer.array();
		byte result = order == ByteOrder.LITTLE_ENDIAN ? data[0] : data[1];
		return result;
	}	

}
