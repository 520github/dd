package me.twocoffee.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.csource.fastdfs.DownloadCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DowloadCallback test
 * 
 * @author Happy Fish / YuQing
 * @version Version 1.3
 */
public class DownloadResponseWriter implements DownloadCallback {

	private static final Logger logger = LoggerFactory
			.getLogger(DownloadResponseWriter.class);

	private long current_bytes = 0;

	private final HttpServletResponse response;

	public DownloadResponseWriter(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public int recv(long file_size, byte[] data, int bytes) {

		try {
			response.getOutputStream().write(data, 0, bytes);
			this.current_bytes += bytes;

			if (this.current_bytes == file_size) {
				response.getOutputStream().flush();
				logger.debug("" + this.current_bytes + "|" + file_size);
			}

		} catch (IOException ex) {
			logger.error("download file error", ex);
			return -1;
		}
		return 0;
	}
}
