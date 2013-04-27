package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.DeviceDao;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.WebToken.UserType;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.query.Query;
import com.mongodb.BasicDBObject;

@org.springframework.stereotype.Repository
public class DeviceDaoImpl extends BaseDao<Device> implements DeviceDao {

    private final static Logger logger = LoggerFactory
	    .getLogger(BadgeDaoImpl.class);

    private static void copyDeviceProp(Device from, Device to) {
	to.setAccountId(from.getAccountId());
	to.setClientVersion(from.getClientVersion());
	to.setDeviceToken(from.getDeviceToken());
	to.setOs(from.getOs());
	to.setOsVersion(from.getOsVersion());
	to.setPushAlert(from.isPushAlert());
	to.setPushURI(from.getPushURI());
    }

    @Override
    public List<Device> findByAccountId(String accountId) {
	Query<Device> q = this.dataStore.createQuery(Device.class)
		.field("accountId").equal(accountId);
	return q.asList();
    }

    @Override
    public Device findById(String id) {
	return this.dataStore.createQuery(Device.class)
		.field("id").equal(id).get();
    }

    @Override
    public Device findByMac(String mac) {
	return this.dataStore.createQuery(Device.class).field("mac").equal(mac)
		.get();
    }

    private Device findByMacOrderByDate(String mac) {
	return this.dataStore.createQuery(Device.class).field("mac").equal(mac)
		.order("-date")
		.get();
    }

    @Override
    public void bindAccount(String id, String accountId) {
    	this.bindAccount(id, accountId, UserType.LoginDuoduo);
    }
    
    @Override
    public void bindAccount(String id, String accountId,UserType userType) {
    	if (StringUtils.isBlank(accountId)) {
    	    return;
    	}
    	Device device = this.findById(id);
    	if (device == null) {
    	    return;
    	}
    	device.setAccountId(accountId);
    	device.setLastModified(new Date());
    	device.setUserType(userType);
    	super.save(device);
    }

    @Override
    public void unbindAcount(String id) {
	Device device = this.findById(id);
	if (device == null) {
	    return;
	}
	device.setAccountId("");
	device.setLastModified(new Date());
	super.save(device);
    }

    @Override
    public void save(Device device) {
	if (device == null) {
	    return;
	}
	Device org = findByMac(device.getMac());
	if (org != null) {// 如果之前就有这个mac的记录的device，替代为新的属性
	    if (org.getAccountId().equals(device.getAccountId())) {
		copyDeviceProp(device, org);
	    } else {
		logger.info(
			"Account:[{}] changed to [{}] on Device[{}]",
			new Object[] { org.getAccountId(),
				device.getAccountId(), org.getId() });
		copyDeviceProp(device, org);
	    }
	    super.save(org);
	} else {
	    device.setId(new ObjectId().toString());
	    device.setDate(new Date());
	    super.save(device);
	}
    }

    @Override
    public Device createOrSave(Device device) {
	if (device == null) {
	    return null;
	}
	Device tempDevice = this.getById(device.getId());
	if (StringUtils.isBlank(device.getId()) || tempDevice == null) {
	    device.setId(new ObjectId().toString());
	    device.setDate(new Date());
	    device.setLastModified(device.getDate());
	    device.setUserType(UserType.Guest);
	}
	else {
		if(StringUtils.isNotBlank(device.getDeviceToken())) {
			tempDevice.setDeviceToken(device.getDeviceToken());
			tempDevice.setLastModified(new Date());
		}
		device = tempDevice;
	}
	super.save(device);
	return device;
    }

    @Override
    public List<Device> findDistinctDeviceByAccountId(String accountId) {
	@SuppressWarnings("unchecked")
	List<String> macs = this.dataStore.getCollection(entityClass).distinct(
		"mac", new BasicDBObject("accountId", accountId));

	List<Device> devices = new ArrayList<Device>(5);

	if (macs != null) {

	    for (String mac : macs) {
		Device d = findByMacOrderByDate(mac);

		if (accountId.equals(d.getAccountId())) {
		    devices.add(d);
		}
	    }
	}
	return devices;
    }
}
