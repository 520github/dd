/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.Date;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.DocumentDao;
import me.twocoffee.entity.Document;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

/**
 * @author momo
 * 
 */
@Repository
public class DocumentDaoImpl extends BaseDao<Document> implements DocumentDao {

	private Date getBeginDateOfToday() {
		Date today = new Date();
		Date d = DateUtils.setHours(today, 0);
		d = DateUtils.setMinutes(d, 0);
		d = DateUtils.setSeconds(d, 0);
		return d;
	}

	@Override
	public long countByDaily(String account) {
		return createQuery().filter("accountId", account)
				.filter("createDate >", getBeginDateOfToday()).countAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.twocoffee.dao.DocumentDao#getDocumentById(java.lang.String)
	 */
	@Override
	public Document getDocumentById(String id) {
		return this.getById(id);
	}
}
