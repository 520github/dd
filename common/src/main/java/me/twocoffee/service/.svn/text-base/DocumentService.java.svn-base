/**
 * 
 */
package me.twocoffee.service;

import java.util.Collection;
import java.util.Map;

import me.twocoffee.entity.Document;

/**
 * @author momo
 * 
 */
public interface DocumentService {

	int getDailyUpload(String account);

	Document getDocumentById(String id);

	Map<String, String> getLimitation(String id);

	String getMimeType(String postfix);

	Collection<String> getWhitlist();

	void save(Document doc);

	/**
	 * @return
	 */
	Collection<String> getMimeTypes();

	/**
	 * @param type
	 * @return
	 */
	boolean checkMimeType(String type);
}
