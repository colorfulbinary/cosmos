package com.cosmos.utils.io;

import java.math.BigInteger;

import com.cosmos.utils.io.exception.BitTypeFormatException;


/*
 * ClassName : BitDataParser
 * Creator   : rouies
 * Function  : 实现java下的bit数据解析功能
 * Date      : 2015-9-11
 */
public enum BitDataParser {
	SIGNED(true),UNSIGNED(false);
	BitDataParser(boolean isSignedParser){
		this.isSignedParser = isSignedParser;
	}
	private boolean isSignedParser;
	
	/*
	 * MethodName : readFromByteArray
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现读取指定字节中的指定位置和长度的bit数据(包含有符号和无符号)
	 * Arguments  : byte[] data    => 要读取的字节数组
	 * 				int startIndex => 要读取字节的起始点(bit),
	 * 				int length     => 要读取数据的长度
	 * Return     : 读取后的数据值
	 */
	public BigInteger readFromByteArray(byte[] data,int startIndex,int length) throws BitTypeFormatException{
		BigInteger buffer = null;
		if(data == null || data.length == 0){
			throw new BitTypeFormatException("要解析的数组不能为空");
		}
		if(startIndex < 0 || startIndex > data.length * 8 - 1){
			throw new BitTypeFormatException("起始长度越界");
		}
		if(startIndex + length > data.length * 8){
			throw new BitTypeFormatException("截取长度超长");
		}
		//字节位置
		int startByteIndex = startIndex / 8;
		int pos = startIndex % 8;
		//占用字节数
		int byteCount = (int)Math.ceil((pos + length) / 8.0);
		//bit偏移量
		int endLength = (pos + length) % 8;
		endLength = endLength == 0 ? 8 : endLength;
		//无符号
		buffer = new BigInteger(new byte[]{data[startByteIndex]});
		for (int i = 0 ; i < byteCount ; i++) {
			if(i == 0){
				int bData = (int)Math.pow(2, (8 - pos)) -1;
				buffer = buffer.and(BigInteger.valueOf(bData));
				if(i == byteCount -1){
					buffer = buffer.shiftRight(8 - endLength);
				}
				
			} else {
				buffer = buffer.shiftLeft(8).or(new BigInteger(new byte[]{data[startByteIndex + i]}));
				if(i == byteCount -1){
					buffer = buffer.shiftRight(8 - endLength);
				}
			}
		}
		if(isSignedParser){
			//如果是有符号的算法
			int bitLength = buffer.bitLength();
			if(bitLength == length){
				//先补位
				int b = ((int)Math.ceil(bitLength / 8.0)) * 8;
				byte[] dest  = buffer.toByteArray();
				if(b == bitLength){
					dest[0] = -1;
				}  else {
					byte a = -1;
					a <<= (8 -(b-length));
					dest[0] |= a;
				}
				buffer = new BigInteger(dest);
			}
		}
		return buffer;
	}
	
	
	/*
	 * MethodName : writeToByteArray
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现按指定的位数和位置将指定长度的整数写入字节数组中
	 * Arguments  : byte[] dest     => 要写入的字节数组
	 * 				int startIndex  => 要写入字节的起始点(bit),
	 * 				int length      => 要写入数据的长度
	 * 				BigInteger data => 要写入数据
	 * Return     : 写入之后，下一次开始的索引号
	 */
	public int writeToByteArray(byte[] dest,int startIndex,int length,BigInteger data) throws BitTypeFormatException{
		//先计算最大值
		BigInteger min = null;
		BigInteger max = null;
		if (isSignedParser) {
			min = BigInteger.valueOf(2).pow(length-1).negate();
			max = BigInteger.valueOf(2).pow(length-1).subtract(BigInteger.valueOf(1));
		} else {
			min = BigInteger.valueOf(0);
			max = BigInteger.valueOf(2).pow(length).subtract(BigInteger.valueOf(1));
		}
		if(data == null){
			throw new BitTypeFormatException("数值不能为空");
		}
		if(data.compareTo(min) < 0){
			throw new BitTypeFormatException("最小值超出长度范围：" + min);
		}
		if(data.compareTo(max) > 0){
			throw new BitTypeFormatException("最大值超出长度范围：" + max);
		}
		if(Math.ceil((startIndex + length) / 8.0) > dest.length){
			throw new BitTypeFormatException("缓冲区过小，无法写入数据");
		}
		//字节位置
		int startByteIndex = startIndex / 8;
		int pos = startIndex % 8;
		int blen = (int)Math.ceil(length / 8.0);
		int bpos = length % 8;
		byte[] ba = data.toByteArray();
		byte[] buffer = new byte[blen];
		for(int i=buffer.length -1,j=ba.length-1;i>=0;i--,j--){
			if(j < 0){
				break;
			}
			buffer[i] = ba[j];
		}
		if(8-pos > bpos){
			buffer = this.byteArrayShiftLeft(buffer, 8-bpos-pos);
		} else if(8-pos < bpos){
			buffer = this.byteArrayShiftRight(buffer, pos + bpos -8);
		}
		buffer[0] &= (-1 >>> pos);
		for (int i = 0,len = buffer.length; i < len; i++) {
			dest[startByteIndex + i] |= buffer[i]; 
		}
		return startIndex + length;
	}
	
	/*
	 * MethodName : byteArrayShiftLeft
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现字节数组向左进行位移(<<),并且将溢出的数字保留(该方法只能位移8位以内，超过8位的按mod计算)
	 * Arguments  : byte[] data     => 要进行位移的数组
	 * 				int num  		=> 位移数
	 * Return     : 位移后的字节数组
	 */
	private byte[] byteArrayShiftLeft(byte[] data,int num){
		num = num % 8;
		byte[] dest = new byte[data.length + 1];
		int fnum = 8 - num; 
		byte bitTmp = (byte)(Math.pow(2,num)-1);
		byte tmp = 0;
		for(int i=data.length -1 ;i >= 0;i--){
			dest[i+1] = (byte) (data[i] << num);
			dest[i+1] |= tmp;
			tmp = (byte) ((data[i] >>> fnum) & bitTmp);
		}
		dest[0] = tmp;
		return dest;
	}
	
	/*
	 * MethodName : byteArrayShiftRight
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 实现字节数组向右进行带符号位移(>>>),并且将溢出的数字保留(该方法只能位移8位以内，超过8位的按mod计算)
	 * Arguments  : byte[] data     => 要进行位移的数组
	 * 				int num  		=> 位移数
	 * Return     : 位移后的字节数组
	 */
	private byte[] byteArrayShiftRight(byte[] data,int num){
		num %= 8;
		byte[] dest = new byte[data.length + 1];
		int fnum = 8 - num; 
		byte bitTmp = -1;
		bitTmp <<= fnum;
		byte tmp = 0;
		for(int i=0,len = data.length;i<len;i++){
			dest[i] = (byte) (data[i] >>> num & ~bitTmp);
			dest[i] |= tmp;
			tmp = (byte) ((data[i] << fnum) & bitTmp);
		}
		dest[dest.length - 1] = tmp;
		return dest;
	}
	
}
