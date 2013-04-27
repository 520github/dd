package me.twocoffee.rest;

import java.io.IOException;
import java.io.InputStream;

public interface StorageRecord {

	public String getId();

	public String getMediaType();

	public InputStream getData() throws IOException;

	public long length();

	public String getName();
}
