package com.cosmos.utils.io;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.cosmos.utils.io.exception.SignedTypeFormatException;


/*
 * ClassName : SignedDataTypeConverter
 * Creator   : rouies
 * Function  : 实现java下的有符号与字节之间的转换
 * Date      : 2015-8-15
 */
public class SignedDataTypeConverter {
	//默认字节序列
	private static ThreadLocal<ByteOrder> v_order = new ThreadLocal<ByteOrder>(){
		protected ByteOrder initialValue() {
			return  ByteOrder.BIG_ENDIAN;
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
		SignedDataTypeConverter.v_order.set(order);
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将16位整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				short number -> 要转换的16位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(short number,ByteOrder order){
		ByteBuffer buffer = ByteBuffer.allocate(2).order(order).putShort(number);
		return buffer.array();
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将32位整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				 number -> 要转换的32位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(int number,ByteOrder order){
		ByteBuffer buffer = ByteBuffer.allocate(4).order(order).putInt(number);
		return buffer.array();
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将64位整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				long number -> 要转换的64位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(long number,ByteOrder order){
		ByteBuffer buffer = ByteBuffer.allocate(8).order(order).putLong(number);
		return buffer.array();
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				BigInteger number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(BigInteger number,ByteOrder order){
		byte[] data = number.toByteArray();
		if (order == ByteOrder.LITTLE_ENDIAN) {
			ArraysUtils.reverseArray(data);
		}
		return data;
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				float number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(float number,ByteOrder order){
		ByteBuffer buffer = ByteBuffer.allocate(4).order(order).putFloat(number);
		return buffer.array();
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，将整数转换为相应的字节序
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				double number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(double number,ByteOrder order){
		ByteBuffer buffer = ByteBuffer.allocate(8).order(order).putDouble(number);
		return buffer.array();
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将16位整数转换为相应的字节序
	 * Arguments  : short number -> 要转换的16位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(short number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将32位整数转换为相应的字节序
	 * Arguments  : int number -> 要转换的32位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(int number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将64位整数转换为相应的字节序
	 * Arguments  : long number -> 要转换的64位整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(long number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将整数转换为相应的字节序
	 * Arguments  : float number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(float number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将整数转换为相应的字节序
	 * Arguments  : double number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(double number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toByte
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认的字节序，将整数转换为相应的字节序
	 * Arguments  : BigInteger number -> 要转换的整数
	 * Return     : 转换后的字节序
	 */
	public static byte[] toByte(BigInteger number){
		return toByte(number,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toUInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换为16位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static short toInt16(byte[] data,ByteOrder order) throws SignedTypeFormatException{
		BigInteger buffer = SignedDataTypeConverter.toInt(data,order);
		if(buffer.compareTo(BigInteger.valueOf(Short.MAX_VALUE)) > 0){
			throw new SignedTypeFormatException("类型数值溢出！");
		}
		return buffer.shortValue();
	}
	
	/*
	 * MethodName : toUInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换为32位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static int toInt32(byte[] data,ByteOrder order) throws SignedTypeFormatException{
		BigInteger buffer = SignedDataTypeConverter.toInt(data,order);
		if(buffer.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0){
			throw new SignedTypeFormatException("类型数值溢出！");
		}
		return buffer.intValue();
	}
	
	/*
	 * MethodName : toUInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换为64位整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static long toInt64(byte[] data,ByteOrder order) throws SignedTypeFormatException{
		BigInteger buffer = SignedDataTypeConverter.toInt(data,order);
		if(buffer.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0){
			throw new SignedTypeFormatException("类型数值溢出！");
		}
		return buffer.longValue();
	}
	
	/*
	 * MethodName : toFloat
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换为float
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static float toFloat(byte[] data,ByteOrder order) throws SignedTypeFormatException{
		if(data == null || data.length > 4){
			throw new SignedTypeFormatException("类型数值溢出！");
		}
		ByteBuffer buffer = ByteBuffer.allocate(4).order(order).put(data);
		buffer.rewind();
		return buffer.getFloat();
	}
	
	/*
	 * MethodName : toDouble
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换为double
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static double toDouble(byte[] data,ByteOrder order) throws SignedTypeFormatException{
		if(data == null || data.length > 8){
			throw new SignedTypeFormatException("类型数值溢出！");
		}
		ByteBuffer buffer = ByteBuffer.allocate(8).order(order).put(data);
		buffer.rewind();
		return buffer.getDouble();
	}
	
	/*
	 * MethodName : toUInt
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用指定的字节序，字节数组转换整数
	 * Arguments  : ByteOrder order -> 转换使用的字节序
	 * 				byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static BigInteger toInt(byte[] data,ByteOrder order){
		if (order == ByteOrder.LITTLE_ENDIAN) {
			//biginteger默认是大端序
			ArraysUtils.reverseArray(data);
		}
		BigInteger buffer = new BigInteger(data);
		return buffer;
	}
	
	/*
	 * MethodName : toInt16
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换16位整数
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static short toInt16(byte[] data) throws SignedTypeFormatException{
		return toInt16(data,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toInt32
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换32位整数
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static int toInt32(byte[] data) throws SignedTypeFormatException{
		return toInt32(data,SignedDataTypeConverter.v_order.get());
	}
	
	
	/*
	 * MethodName : toInt64
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换64位整数
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static long toInt64(byte[] data) throws SignedTypeFormatException{
		return toInt64(data,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toFloat
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换float
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static float toFloat(byte[] data) throws SignedTypeFormatException{
		return toFloat(data,SignedDataTypeConverter.v_order.get());
	}
	
	/*
	 * MethodName : toDouble
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换double
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static double toDouble(byte[] data) throws SignedTypeFormatException{
		return toDouble(data,SignedDataTypeConverter.v_order.get());
	}
	/*
	 * MethodName : toInt
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 使用默认字节序，字节数组转换整数
	 * Arguments  : byte[] number -> 要转换的字节数组
	 * Return     : 转换后的整数
	 */
	public static BigInteger toInt(byte[] data){
		return toInt(data,SignedDataTypeConverter.v_order.get());
	}	
	
}
