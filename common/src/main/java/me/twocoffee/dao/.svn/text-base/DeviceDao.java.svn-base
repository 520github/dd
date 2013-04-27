package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Device;
import me.twocoffee.entity.WebToken.UserType;

public interface DeviceDao {
    /**
     * 根据账户查询设备
     * 
     * @param accountId
     * @return
     */
    public List<Device> findByAccountId(String accountId);

    /**
     * 根据clientId查询设备
     * 
     * @param id
     * @return
     */
    public Device findById(String id);

    /**
     * 按mac查询device
     * 
     * @param mac
     * @return
     */
    public Device findByMac(String mac);

    /**
     * 保存一个设备信息 如果设备中的mac已经在表中存在，则需要把之前的device记录删掉
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
    public void bindAccount(String id, String accountId,UserType userType);
    

    /**
     * 解除绑定账号
     * 
     * @param id
     */
    public void unbindAcount(String id);

    public List<Device> findDistinctDeviceByAccountId(String accountId);

}
