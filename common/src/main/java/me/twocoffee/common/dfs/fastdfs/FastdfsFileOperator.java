package me.twocoffee.common.dfs.fastdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import me.twocoffee.common.SpringContext;
import me.twocoffee.common.dfs.FileInfo;
import me.twocoffee.common.dfs.FileOperator;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.DownloadCallback;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastdfsFileOperator implements FileOperator {
	protected String configFile = null;
	protected Properties configProperties = null;
	protected boolean isReady = false;

	public FastdfsFileOperator(String configFile) throws IOException {
		this.configFile = SpringContext.getRealPath(configFile);
		loadConfig(this.configFile);
		isReady = initServer(this.configFile);
	}

	private TrackerServer connectServer() {
		TrackerClient tracker = new TrackerClient();
		try {
			return tracker.getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private byte[] getFileBuffer(InputStream stream, long size)
			throws IOException {
		byte[] buff = new byte[(int) size];
		int read = stream.read(buff);
		if (read != (int) size)
			throw new IOException("Read file error.");
		return buff;
	}

	private String getFileExtName(String fileName) {
		if (fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	protected boolean initServer(String configFile) {
		try {
			ClientGlobal.init(configFile);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	protected void loadConfig(String f) throws IOException {
		configProperties = new Properties();
		try {
			configProperties.load(new FileInputStream(f));
		} catch (IOException e) {
			configProperties = null;
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public byte[] getFile(String fileId) throws Exception {
		if (!isReady) {
			isReady = initServer(configFile);
		}
		if (!isReady)
			throw new IOException("Can't init dfs server.");

		TrackerServer trackerServer = connectServer();
		if (trackerServer == null)
			throw new IOException("Can't connect dfs server.");

		// 上传文件
		try {
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer,
					storageServer);

			return client.download_file1(fileId);
		} finally {
			if (trackerServer != null) {
				trackerServer.close();
			}
		}
	}

	@Override
	public void getFile(String fileId, long offset, long length,
			DownloadCallback callback) throws Exception {

		if (!isReady) {
			isReady = initServer(configFile);
		}

		if (!isReady)
			throw new IOException("Can't init dfs server.");

		TrackerServer trackerServer = connectServer();

		if (trackerServer == null)
			throw new IOException("Can't connect dfs server.");

		try {
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer,
					storageServer);

			if (client.download_file1(fileId, offset, length, callback) != 0) {
				throw new Exception("download failed!file id [" + fileId + "]");
			}
		} finally {
			trackerServer.close();
		}
	}

	@Override
	public FileInfo getFileInfo(String fileId) throws Exception {
		if (!isReady) {
			isReady = initServer(configFile);
		}
		if (!isReady)
			throw new IOException("Can't init dfs server.");

		TrackerServer trackerServer = connectServer();
		if (trackerServer == null)
			throw new IOException("Can't connect dfs server.");

		try {
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer,
					storageServer);

			// 上传文件
			org.csource.fastdfs.FileInfo info = null;

			info = client.get_file_info1(fileId);

			if (info == null)
				return null;

			FileInfo fi = new FileInfo();
			fi.setCreated(info.getCreateTimestamp());
			fi.setSize(info.getFileSize());
			return fi;
		} finally {
			if (trackerServer != null) {
				trackerServer.close();
			}
		}
	}

	@Override
	public String getFileUrl(String fileId) {
		String server = configProperties.getProperty("http.server");
		if (server == null)
			throw new IllegalArgumentException("Not config http.server.");

		if (!server.endsWith("/")) {
			server += "/";
		}
		return server + fileId;
	}

	@Override
	public String putFile(byte[] fileBuff, String name) throws IOException {
		if (!isReady) {
			isReady = initServer(configFile);
		}
		if (!isReady)
			throw new IOException("Can't init dfs server.");

		TrackerServer trackerServer = connectServer();
		if (trackerServer == null)
			throw new IOException("Can't connect dfs server.");

		// 上传文件
		try {
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer,
					storageServer);

			String ext = getFileExtName(name);

			return client.upload_file1(fileBuff, ext, null);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (trackerServer != null) {
				trackerServer.close();
			}
		}
	}

	@Override
	public String putFile(File file) throws FileNotFoundException, IOException {
		FileInputStream fio = null;
		try {
			fio = new FileInputStream(file);

			return putFile(fio, file.length(), file.getName());
		} finally {
			if (fio != null) {
				try {
					fio.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String putFile(InputStream stream, long size, String name)
			throws IOException {
		if (!isReady) {
			isReady = initServer(configFile);
		}
		if (!isReady)
			throw new IOException("Can't init dfs server.");

		TrackerServer trackerServer = connectServer();
		if (trackerServer == null)
			throw new IOException("Can't connect dfs server.");

		// 上传文件
		try {
			StorageServer storageServer = null;
			StorageClient1 client = new StorageClient1(trackerServer,
					storageServer);

			String ext = getFileExtName(name);

			int len = (int) size;
			byte[] all = new byte[len];
			int readCount = 0;
			int index = 0;
			do {
				readCount = stream.read(all, index, len);
				index += readCount;
				len -= readCount;
			} while (len > 0);

			return client.upload_file1(all, ext, null);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (trackerServer != null) {
				trackerServer.close();
			}
		}
	}

}
