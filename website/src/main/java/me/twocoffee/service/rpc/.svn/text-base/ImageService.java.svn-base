package me.twocoffee.service.rpc;

import java.util.Map;

public interface ImageService {

	public Map<String, String> compressImage(byte b[]) throws Exception;

	public Map<String, String> cropImage(int x, int y, int w, int h,
			String imageId)
			throws Exception;

	/**
	 * firefox image/jpeg image/bmp image/gif image/png ie 6 image/pjpeg *
	 * image/bmp image/gif image/x-png ie 7 image/pjpeg image/bmp image/gif *
	 * image/x-png ie 8 image/pjpeg image/bmp image/gif image/x-png
	 * 
	 * @param contentType
	 *            浏览器上传的type
	 * @return 后缀
	 */
	public String getSuffixByContentType(String contentType);

}
