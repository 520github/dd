package me.twocoffee.service.impl;

import me.twocoffee.dao.ContentDao;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.ContentAction;
import me.twocoffee.service.ContentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentDao contentDao;

	@Override
	public Content getById(String id) {
		return contentDao.getById(id);
	}

	@Override
	public Content getByUrl(String url) {
		return contentDao.getByUrl(url);
	}

	@Override
	public Content getByUrlAndType(String url, ContentType type) {
		return contentDao.getByUrlAndContentType(url, type);
	}

	public ContentDao getContentDao() {
		return contentDao;
	}

	@Override
	public void save(Content content) {
		contentDao.save(content);
	}

	@Override
	public void saveContentAction(ContentAction ca) {
		contentDao.saveContentAction(ca);
	}

	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

}
