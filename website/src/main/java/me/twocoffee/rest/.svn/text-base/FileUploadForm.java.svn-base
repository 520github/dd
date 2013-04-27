/**
 * 
 */
package me.twocoffee.rest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

/**
 * @author momo
 * 
 */
public class FileUploadForm {

	private File file;

	private String fileName;

	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	public long getSize() {

		if (data != null) {
			return data.length;
		}

		if (file != null) {
			return file.length();
		}
		return 0;
	}

	@FormParam("uploadedFile")
	@PartType("application/octet-stream")
	public void setData(byte[] data) {
		this.data = data;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@FormParam("fileName")
	@PartType("text/plain")
	public void setFileName(String fileName) {

		try {
			this.fileName = URLDecoder.decode(fileName, "utf-8");

		} catch (UnsupportedEncodingException e) {
			this.fileName = fileName;

		} catch (Exception e1) {
			this.fileName = fileName;
		}
	}

}
