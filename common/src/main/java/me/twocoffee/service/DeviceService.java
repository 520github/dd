package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Device;
import me.twocoffee.entity.WebToken.UserType;

public interface DeviceService {
    /**
     * 根据账户id查询该账户登录的设备
     * 
     * @param accountId
     * @return
     */
    public List<Device> getByAccountId(String accountId);

    /**
     * 根据帐户id查询该帐户登录的设备。结果中去掉重复的设备。
     * 
     * @param accountId
     * @return
     */
    public List<Device> getDistinctDeviceByAccountId(String accountId);

    /**
     * 根据clientId查询设备
     * 
     * @param id
     * @return
     */
    public Device getById(String id);

    /**
     * 保存一个设备信息 如果设备中的mac已经在表中存在，则需要把之前的device记录删掉
     * 
     * @param device
     */
    public void save(Device device);

    public Device createOrSave(Device device);

    /**
     * 绑定账号
     * 
     * @param id
     * @param accountId
     *            账号ID
     */
    public void bindAccount(String id, String accountId);
    
    /**
     * 绑定账号
     * 
     * @param id
     * @param accountId 账号ID
     * @param userType          
     */
    public void bindAccount(String id, String accountId, UserType userType);

    /**
     * 解除绑定账号
     * 
     * @param id
     */
    public void unbindAcount(String id);
}
