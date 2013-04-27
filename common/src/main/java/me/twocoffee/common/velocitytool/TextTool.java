package me.twocoffee.common.velocitytool;

public class TextTool {
	public String subString(String str, int size) {
		return subString(str, size, 0);
	}
	
	public String subString(String str, int size, int dotSize) {
		if (str == null || "".equals(str) || size < 1) {  
            return "";  
        }  
        char[] chars = str.toCharArray();  
        int count = 0;  
        int normalCharsLength = 0;
        boolean inet = false;
        for (int i = 0; i < chars.length; i++) {  
            int specialCharLength = getSpecialCharLength(chars[i]);  
            if (count <= size - specialCharLength) {  
                count += specialCharLength;  
                normalCharsLength++;  
            } else {
            	inet = true;
                break;
            }  
        }  
        String ret = new String(chars, 0, normalCharsLength);
        return inet ? ret + getDotString(dotSize) : ret;
	}
	
	private String getDotString(int dotSize) {
		if (dotSize < 1)
			return "";
		String s = "";
		for (int i=0; i<dotSize; i++) {
			s += ".";
		}
		return s;
	}
	
	/** 
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1 
     * @param c 字符 
     * @return 字符长度 
     */  
    private static int getSpecialCharLength(char c) {  
        if (isLetter(c)) {  
            return 1;  
        } else {  
            return 2;  
        }  
    }  
  
    /** 
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符） 
     *  
     * @param char c, 需要判断的字符 
     * @return boolean, 返回true,Ascill字符 
     */  
    private static boolean isLetter(char c) {  
        int k = 0x80;
        return c / k == 0 ? true : false;
    }
}
