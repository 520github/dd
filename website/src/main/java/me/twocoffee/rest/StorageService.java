package me.twocoffee.rest;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

	String store(InputStream in, String mediaType, String name)
			throws IOException;

	StorageRecord load(String id) throws IOException;
}
