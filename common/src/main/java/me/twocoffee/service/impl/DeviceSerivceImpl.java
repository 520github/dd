package me.twocoffee.service.impl;

import java.util.List;

import me.twocoffee.dao.DeviceDao;
import me.twocoffee.entity.Device;
import me.twocoffee.entity.WebToken.UserType;
import me.twocoffee.service.DeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceSerivceImpl implements DeviceService {

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public List<Device> getByAccountId(String accountId) {
	return deviceDao.findByAccountId(accountId);
    }

    @Override
    public Device getById(String id) {
	return deviceDao.findById(id);
    }

    @Override
    public void save(Device device) {
	deviceDao.save(device);
    }

    @Override
    public Device createOrSave(Device device) {
	return deviceDao.createOrSave(device);
    }

    @Override
    public void bindAccount(String id, String accountId) {
	deviceDao.bindAccount(id, accountId);
    }
    
    public void bindAccount(String id, String accountId, UserType userType) {
    	deviceDao.bindAccount(id, accountId, userType);
    }

    @Override
    public void unbindAcount(String id) {
	deviceDao.unbindAcount(id);
    }

    @Override
    public List<Device> getDistinctDeviceByAccountId(String accountId) {
	return deviceDao.findDistinctDeviceByAccountId(accountId);
    }

}
