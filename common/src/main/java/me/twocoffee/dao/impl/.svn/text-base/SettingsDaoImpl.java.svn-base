/**
 * 
 */
package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.SettingsDao;
import me.twocoffee.entity.Settings;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class SettingsDaoImpl extends BaseDao<Settings> implements SettingsDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.twocoffee.dao.SettingsDao#getSettings(java.lang.String)
	 */
	@Override
	public Settings getSettings(String account) {
		return createQuery().filter("account =", account).get();
	}

}
