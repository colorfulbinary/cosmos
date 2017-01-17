package com.cosmos.utils.io;

import java.util.Arrays;


/*
 * ClassName : ArraysUtils
 * Creator   : rouies
 * Function  : 数组相关方法
 * Date      : 2015-8-15
 */
public class ArraysUtils {
	/*
	 * MethodName : reverseArray
	 * MethodType : static
	 * Creator    : rouies
	 * Function   : 数组转置方法
	 * Arguments  : T[] array -> 要转置的数组
	 * Return     : void
	 */
	public static <T> void reverseArray(T[] array){
		T tmp = null;
		int start = 0;
		int end = array.length -1;
		while(start < end){
			tmp = array[start];
			array[start] = array[end];
			array[end] = tmp;
			start++;
			end--;
		}
	}
	
	/*
	 * MethodName : byteArrayShiftLeft
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现字节数组向左进行位移(<<)
	 * Arguments  : byte[] data     => 要进行位移的数组
	 * 				int num  		=> 位移数
	 * Return     : 位移后的字节数组
	 */
	public static void byteArrayShiftLeft(byte[] buffer,int num){
		int scount = num / 8;
		byte[] dest = new byte[buffer.length];
		if(buffer.length > scount){
			byte[] tmpArray = Arrays.copyOfRange(buffer,scount, buffer.length);
			num %= 8;
			int fnum = 8 - num; 
			byte bitTmp = (byte)(Math.pow(2,num)-1);
			byte tmp = 0;
			for(int i=tmpArray.length -2 ;i >= 0;i--){
				dest[i+1] = (byte) (tmpArray[i] << num);
				dest[i+1] |= tmp;
				tmp = (byte) ((tmpArray[i] >>> fnum) & bitTmp);
			}
			System.arraycopy(dest, 0, buffer, scount, dest.length);
		} else {
			Arrays.fill(buffer, (byte)0);
		}
	}
	
	
	/*
	 * MethodName : byteArrayShiftRight
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现字节数组向右进行带符号位移(>>>)
	 * Arguments  : byte[] data     => 要进行位移的数组
	 * 				int num  		=> 位移数
	 * Return     : 位移后的字节数组
	 */
	public static void byteArrayShiftRight(byte[] buffer,int num){
		int scount = num / 8;
		if(buffer.length > scount){
			byte[] tmpArray = Arrays.copyOfRange(buffer, 0, buffer.length - scount);
			num %= 8;
			byte[] dest = new byte[buffer.length];
			int fnum = 8 - num; 
			byte bitTmp = -1;
			bitTmp <<= fnum;
			byte tmp = 0;
			for(int i=0,len = tmpArray.length;i<len;i++){
				dest[i] = (byte) (tmpArray[i] >>> num & ~bitTmp);
				dest[i] |= tmp;
				tmp = (byte) ((tmpArray[i] << fnum) & bitTmp);
			}
			System.arraycopy(dest, 0, buffer, scount, dest.length);
		} else {
			Arrays.fill(buffer, (byte)0);
		}
		
	}
	
	static void reverseArray(byte[] array){
		byte tmp = 0;
		int start = 0;
		int end = array.length -1;
		while(start < end){
			tmp = array[start];
			array[start] = array[end];
			array[end] = tmp;
			start++;
			end--;
		}
	}
	
	public static boolean byteArrayEquals(byte[] a1,byte[] a2){
		boolean result = true;
		if(a1 == a2){
			return true;
		}
		if(a1 == null || a2 == null || a1.length != a2.length){
			return false;
		}
		
		for(int i=0,len = a1.length;i<len;i++){
			if(a1[i]!=a2[i]){
				result = false;
				break;
			}
		}
		return result;
	}
	
}
