/**
 * 
 */
package me.twocoffee.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import me.twocoffee.common.util.DateUtil;
import me.twocoffee.dao.DocumentDao;
import me.twocoffee.dao.SettingsDao;
import me.twocoffee.entity.Document;
import me.twocoffee.entity.Settings;
import me.twocoffee.service.DocumentService;
import me.twocoffee.service.fetch.impl.product.parser.PropertiesFileHodler;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentDao documentDao;

	private Map<String, String> whitelist;

	@Autowired
	private SettingsDao settingsDao;

	@Override
	public int getDailyUpload(String account) {
		Settings settings = settingsDao.getSettings(account);

		if (settings != null) {
			String today = DateUtil.format(new Date(), "yyyy-MM-dd");

			if (settings.getFileUpload().getLimitation().getDailyUploadNumber()
					.containsKey(today)) {

				return settings.getFileUpload().getLimitation()
						.getDailyUploadNumber().get(today);

			} else {
				settings.getFileUpload().getLimitation().getDailyUploadNumber()
						.clear();

				settings.getFileUpload().getLimitation().getDailyUploadNumber()
						.put(today, 0);

				settingsDao.save(settings);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.DocumentService#getDocumentById(java.lang.String)
	 */
	@Override
	public Document getDocumentById(String id) {
		return documentDao.getDocumentById(id);
	}

	@Override
	public Map<String, String> getLimitation(String id) {
		return null;
	}

	@Override
	public String getMimeType(String postfix) {
		return whitelist.get(postfix);
	}

	@Override
	public Collection<String> getWhitlist() {
		return whitelist.keySet();
	}

	@PostConstruct
	public void init() {
		whitelist = new HashMap<String, String>();
		Properties properties = new Properties();
		InputStream in = null;

		try {
			in = PropertiesFileHodler.class
					.getResourceAsStream("/document/whitelist.properties");
			if (in != null) {
				properties.load(in);
			}

			for (String key : properties.stringPropertyNames()) {
				whitelist.put(key, properties.getProperty(key));
			}

		} catch (IOException e) {

		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.DocumentService#save(me.twocoffee.entity.Document)
	 */
	@Override
	public void save(Document doc) {
		documentDao.save(doc);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.service.DocumentService#getMimeTypes()
	 */
	@Override
	public Collection<String> getMimeTypes() {
		return whitelist.values();
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.service.DocumentService#checkMimeType(java.lang.String)
	 */
	@Override
	public boolean checkMimeType(String type) {
		
		for (String mimeType : whitelist.values()) {
			
			if (mimeType.equals(type)) {
				return true;
			}
		}
		return false;
	}

}
