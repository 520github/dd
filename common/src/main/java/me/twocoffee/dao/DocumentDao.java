/**
 * 
 */
package me.twocoffee.dao;

import me.twocoffee.entity.Document;

/**
 * @author momo
 * 
 */
public interface DocumentDao {

	long countByDaily(String account);

	Document getDocumentById(String id);

	void save(Document doc);
}
