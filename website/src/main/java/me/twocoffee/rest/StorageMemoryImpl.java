package me.twocoffee.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.io.ByteStreams;

@Service
@Scope("singleton")
public class StorageMemoryImpl implements StorageService {

	private final ConcurrentHashMap<String, byte[]> storage = new ConcurrentHashMap<String, byte[]>();
	private final ConcurrentHashMap<String, String> contentTypeMap = new ConcurrentHashMap<String, String>();
	private final ConcurrentHashMap<String, String> nameMap = new ConcurrentHashMap<String, String>();

	@Override
	public String store(InputStream in, String contentType, String name)
			throws IOException {
		String id = UUID.randomUUID().toString();
		storage.put(id, ByteStreams.toByteArray(in));
		contentTypeMap.put(id, contentType);
		nameMap.put(id, name);
		return id;
	}

	@Override
	public StorageRecord load(final String id) throws IOException {
		final byte[] buf = storage.get(id);
		if (buf == null) {
			return null;
		} else {
			final String contentType = contentTypeMap.get(id);
			final String name = nameMap.get(id);
			return new StorageRecord() {

				@Override
				public String getMediaType() {
					return contentType;
				}

				@Override
				public String getId() {
					return id;
				}

				@Override
				public InputStream getData() throws IOException {
					return new ByteArrayInputStream(buf);
				}

				@Override
				public long length() {
					return buf.length;
				}

				@Override
				public String getName() {
					return name;
				}
			};
		}
	}
}
