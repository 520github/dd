package me.twocoffee.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

public class SignUtil {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 
	 * @param params
	 * @param secret
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public static String sign(final TreeMap params,
		 String secret) throws UnsupportedEncodingException {
		 if (null == params || params.isEmpty()) {
		 return (String) null;
		 }
		 if (isBlank(secret)) {
		 return (String) null;
		 }
		 StringBuilder sb = new StringBuilder();
		 sb.append(secret);
		 for (Iterator<Entry> it = params.entrySet().iterator(); it
		 .hasNext();) {
		 Entry<String,String> entry = it.next();
		 sb.append(entry.getKey()).append(defaultString(entry.getValue()));
		 }
		 sb.append(secret);
		 byte[] bytes = sb.toString().getBytes("UTF-8");
		 return md5Hex(bytes).toUpperCase();
		 }

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static String defaultString(String str) {
		return str == null ? "" : str;
	}

	private static String md5Hex(byte[] bytes) {
		return new String(encodeHex(getMd5Digest().digest(bytes)));
	}

	private static MessageDigest getMd5Digest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;
		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

	/**
	 * 
	 * @param urlStr
	 * @param content
	 * @return
	 */

	public static String getResult(String urlStr, String content) {
		URL url = null;
		HttpURLConnection connection = null;

		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();

			DataOutputStream out = new DataOutputStream(connection
					.getOutputStream());
			out.write(content.getBytes("utf-8"));
			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

}
