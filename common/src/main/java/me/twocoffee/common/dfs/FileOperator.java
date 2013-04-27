package me.twocoffee.common.dfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.csource.fastdfs.DownloadCallback;

/**
 * 文件操作接口
 * 
 * @author drizzt
 * 
 */
public interface FileOperator {
	/**
	 * 取到文件
	 * 
	 * @param fileId
	 *            文件ID
	 * @return 文件缓冲
	 * @throws Exception
	 */
	byte[] getFile(String fileId) throws Exception;

	/**
	 * 根据设置的偏移量及下载大小，下载文件
	 * 
	 * @param fileId
	 * @param offset
	 *            偏移量
	 * @param length
	 *            下载长度
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	void getFile(String fileId, long offset, long length,
			DownloadCallback callback)
			throws Exception;

	/**
	 * 得到文件信息
	 * 
	 * @param fileId
	 *            文件ID
	 * @return 文件信息
	 * @throws Exception
	 */
	FileInfo getFileInfo(String fileId) throws Exception;

	/**
	 * 得到文件显示的url
	 * 
	 * @param fileId
	 *            文件ID
	 * @return 返回的url
	 */
	String getFileUrl(String fileId);

	/**
	 * 上传文件到服务端
	 * 
	 * @param fileBuff
	 *            文件缓冲
	 * @param name
	 *            文件名
	 * @return 返回文件ID
	 * @throws IOException
	 */
	String putFile(byte[] fileBuff, String name) throws IOException;

	/**
	 * 上传文件到服务端
	 * 
	 * @param file
	 *            文件
	 * @return 返回文件ID
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	String putFile(File file) throws FileNotFoundException, IOException;

	/**
	 * 上传文件到服务端
	 * 
	 * @param stream
	 *            文件流
	 * @param name
	 *            文件名称
	 * @return 返回文件ID
	 * @throws IOException
	 */
	String putFile(InputStream stream, long size, String name)
			throws IOException;
}
