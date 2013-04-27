package me.twocoffee.service.rpc.impl;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.entity.Account;
import me.twocoffee.service.rpc.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;
	public static final String SUFFIXES_JPG = "jpg";
	public static final String SUFFIXES_BMP = "bmp";
	public static final String SUFFIXES_PNG = "png";
	public static final String SUFFIXES_GIF = "gif";

	@Autowired
	private FileOperator fileOperator;

	private String compress(BufferedImage in, int w, int h)
			throws IOException {
		BufferedImage tag = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		tag.getGraphics().drawImage(
				in.getScaledInstance(w, h,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(tag, SUFFIXES_PNG, os);
		String id = fileOperator.putFile(os.toByteArray(), "." + SUFFIXES_PNG);
		os.close();
		return id;
	}

	private BufferedImage crop(int x, int y, int w, int h, String imageid)
			throws Exception {
		byte[] b = fileOperator.getFile(imageid);
		ImageIO.setUseCache(false);
		java.awt.Image srcImage = ImageIO.read(new ByteArrayInputStream(b));
		int width = srcImage.getWidth(null); // 得到源图宽
		int height = srcImage.getHeight(null); // 得到源图长
		double scale = getScale(width, height);// 获得缩放比例
		x = (int) Math.round(x * scale);
		y = (int) Math.round(y * scale);
		w = (int) Math.round(w * scale);
		h = (int) Math.round(h * scale);

		ImageInputStream input = ImageIO
				.createImageInputStream(new ByteArrayInputStream(b));
		Iterator<ImageReader> i = ImageIO.getImageReaders(input);
		BufferedImage bi = null;
		if (i.hasNext()) {
			ImageReader reader = i.next();
			reader.setInput(input);
			ImageReadParam param = reader.getDefaultReadParam();
			Rectangle rect = new Rectangle(x, y, w, h);
			param.setSourceRegion(rect);
			bi = reader.read(0, param);
		}
		input.close();
		return bi;

	}

	private double getScale(int width, int height) {
		if (width > WIDTH || height > HEIGHT) {
			if ((width / (double) height) > (WIDTH / (double) HEIGHT)) {
				return width / (double) WIDTH;
			} else {
				return height / (double) HEIGHT;
			}
		} else {
			return 1;
		}
	}

	@Override
	public Map<String, String> compressImage(byte[] b) throws Exception {
		ImageInputStream input = ImageIO
				.createImageInputStream(new ByteArrayInputStream(b));
		Iterator<ImageReader> i = ImageIO.getImageReaders(input);
		BufferedImage bi = null;
		if (i.hasNext()) {
			ImageReader reader = i.next();
			reader.setInput(input);
			bi = reader.read(0);
		}
		input.close();
		Map<String, String> map = new HashMap<String, String>();
		map.put(Account.PHOTO_SIZE_SMALL, fileOperator.getFileUrl(
				compress(bi, Integer.valueOf(Account.PHOTO_SIZE_SMALL),
						Integer.valueOf(Account.PHOTO_SIZE_SMALL))));
		map.put(Account.PHOTO_SIZE_MIDDLE, fileOperator.getFileUrl(
				compress(bi, Integer.valueOf(Account.PHOTO_SIZE_MIDDLE),
						Integer.valueOf(Account.PHOTO_SIZE_MIDDLE))));
		map.put(Account.PHOTO_SIZE_BIG, fileOperator.getFileUrl(
				compress(bi, Integer.valueOf(Account.PHOTO_SIZE_BIG),
						Integer.valueOf(Account.PHOTO_SIZE_BIG))));
		return map;
	}

	@Override
	public Map<String, String> cropImage(int x, int y, int w, int h,
			String imageId)
			throws Exception {
		BufferedImage bi = crop(x, y, w, h, imageId);
		if (bi != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(Account.PHOTO_SIZE_SMALL, fileOperator.getFileUrl(
					compress(bi, Integer.valueOf(Account.PHOTO_SIZE_SMALL),
							Integer.valueOf(Account.PHOTO_SIZE_SMALL))));
			map.put(Account.PHOTO_SIZE_MIDDLE, fileOperator.getFileUrl(
					compress(bi, Integer.valueOf(Account.PHOTO_SIZE_MIDDLE),
							Integer.valueOf(Account.PHOTO_SIZE_MIDDLE))));
			map.put(Account.PHOTO_SIZE_BIG, fileOperator.getFileUrl(
					compress(bi, Integer.valueOf(Account.PHOTO_SIZE_BIG),
							Integer.valueOf(Account.PHOTO_SIZE_BIG))));
			return map;
		}
		return null;

	}

	@Override
	public String getSuffixByContentType(String contentType) {
		if ("image/pjpeg".equals(contentType)
				|| "image/jpeg".equals(contentType)) {
			return SUFFIXES_JPG;
		} else if ("image/x-png".equals(contentType)
				|| "image/png".equals(contentType)) {
			return SUFFIXES_PNG;
		} else if ("image/bmp".equals(contentType)) {
			return SUFFIXES_BMP;
		} else if ("image/gif".equals(contentType)) {
			return SUFFIXES_GIF;
		} else {
			return "";
		}
	}

}
