/**
 * 
 */
package me.twocoffee.dao;

import me.twocoffee.entity.Settings;

/**
 * @author momo
 * 
 */
public interface SettingsDao {

	Settings getSettings(String account);

	void save(Settings settings);

}
