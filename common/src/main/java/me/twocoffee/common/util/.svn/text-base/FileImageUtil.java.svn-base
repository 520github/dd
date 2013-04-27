package me.twocoffee.common.util;

import me.twocoffee.common.constant.SystemConstant;

public class FileImageUtil {
	private static String FILE_TYPE[] = {"txt","pdf","doc","docx","ppt","pptx","xls","xlsx","wps","epub","zip"};
    private static String FILE_NAME[] = {"TXT.png","PDF.png","DOC.png","DOCX.png","PPT.png","PPTX.png","XLS.png","XLSX.png","WPS.png","EPUB.png","ZIP.png"};
    private static String FILE_IMAGE_PATH = "http://"+SystemConstant.domainName+"/zh_CN/images1/filetype/";
    private static String UNKNOWN_FILE_IMAGE = "UNKNOWN.png";
    
    
    public static String getFileTypePath(String type) {
    	return FILE_IMAGE_PATH + getFileTypeName(type);
    }
    
    public static String getFileTypeName(String type) {
    	String fileName =  UNKNOWN_FILE_IMAGE;
    	for (int i = 0; i < FILE_TYPE.length; i++) {
    	   String fileType = FILE_TYPE[i];
	       if(fileType.equalsIgnoreCase(type)) {
	           try{
	        	   fileName = FILE_NAME[i];
	        	   break;
	           }catch(Exception e){
	           }
	       }
		}
    	return fileName;
    }
    
    public static String getUnkownFileImage() {
    	return FILE_IMAGE_PATH + UNKNOWN_FILE_IMAGE;
    }
}
