package me.twocoffee.service.impl;

import java.util.List;

import me.twocoffee.dao.InviteRecordDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.InviteRecord;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.InviteRecordService;
import me.twocoffee.service.event.AcceptInviteEvent;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class InviteRecordServiceImpl implements InviteRecordService,
		ApplicationContextAware {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private InviteRecordDao inviteRecordDao;
	@Autowired
	private AccountService accountService;

	@Override
	public List<InviteRecord> findByInvitees(String invitees) {
		return inviteRecordDao.findByInvitees(invitees);
	}

	@Override
	public InviteRecord findByInvitor(String invitor) {
		return inviteRecordDao.findByInvitor(invitor);
	}

	@Override
	public List<InviteRecord> findByInvitor(String invitor, int PageSize,
			int PageNum) {
		return inviteRecordDao.findByInvitor(invitor, PageSize, PageNum);
	}

	@Override
	public InviteRecord findByInvitorAndCode(String invitor, String code) {
		return inviteRecordDao.findByInvitorAndCode(invitor, code);
	}

	@Override
	public InviteRecord findByInvitorAndInvitees(String invitor, String invitees) {
		return inviteRecordDao.findByInvitorAndInvitees(invitor, invitees);
	}

	@Override
	public void save(InviteRecord record) {
		inviteRecordDao.save(record);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void verifyInvitation(String loginName) {
		List<InviteRecord> irList = inviteRecordDao.findByInvitees(loginName);
		if (irList != null) {
			for (InviteRecord inviteRecord : irList) {
				inviteRecord.setResult(true);
				inviteRecordDao.save(inviteRecord);
			}
			Account account = accountService.getByLoginName(loginName);
			AcceptInviteEvent event = new AcceptInviteEvent(this,
					account.getId(),
					account.getInviter());
			this.applicationContext.publishEvent(event);
		}

	}

}
