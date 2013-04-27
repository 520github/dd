package me.twocoffee.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.twocoffee.entity.Token;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TokenDaoImplTest {
	private TokenDaoImpl dao;
	private Datastore dataStore;

	@Before
	public void init() {

		Mongo mongo = null;
		try {
			mongo = new Mongo("db.cloud.mduoduo.in", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataStore = new Morphia().createDatastore(mongo, "mduoduo"
				+ getTestDBNameSufix());

		dao = new TokenDaoImpl(dataStore);
	}

	@Test
	public void ListByReferAndCreateTimeTest() {

		Token testTokenOne = getTestToken();

		// 查询条件
		String searchRefer = testTokenOne.getRefer() + "search";
		Date minSearchCreateTime = DateUtils.addMinutes(testTokenOne
				.getCreateTime(), -5);

		// 插入第一条测试数据
		testTokenOne.setRefer(searchRefer);
		dataStore.save(testTokenOne);

		// 再插一个创建时间稍微晚点的用来验证排序是否正确
		Token testTokenTwo = new Token();
		testTokenTwo.setRefer(searchRefer);
		testTokenTwo.setCreateTime(DateUtils.addMinutes(testTokenOne
				.getCreateTime(), 1));
		dataStore.save(testTokenTwo);

		// 插入一条createTime不符合的数据
		Token controlTokenOne = getTestToken();
		controlTokenOne.setRefer(searchRefer);
		controlTokenOne.setCreateTime(DateUtils.addMinutes(minSearchCreateTime,
				-1));
		dataStore.save(controlTokenOne);

		// 插入一条refer不符合的数据
		Token controlTokenTwo = getTestToken();
		controlTokenTwo.setRefer(searchRefer + "diff");
		dataStore.save(controlTokenTwo);

		List<Token> result = dao.listByReferAndCreateTime(searchRefer,
				minSearchCreateTime);

		// 验证结果
		assertTrue(result != null);
		assertEquals(2, result.size());
		assertEquals(testTokenTwo.getId(), result.get(0).getId());
		assertEquals(testTokenOne.getId(), result.get(1).getId());

	}

	@After
	public void after() {
		// clean data
		dataStore.delete(dataStore.createQuery(Token.class));
	}

	protected String getTestDBNameSufix() {
		String hostName = null;
		String hostAddress = null;

		try {
			hostName = InetAddress.getLocalHost().getHostName()
					.replaceAll("\\W", "_");
			hostAddress = InetAddress.getLocalHost().getHostAddress()
					.replaceAll("\\W", "_");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName + "_" + hostAddress;
	}

	private Token getTestToken() {
		Token token = new Token();
		token.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		token.setRefer("testRefer");
		token.setCreateTime(new Date());
		token.setExpiredTime(DateUtils.addHours(token.getCreateTime(), 24));

		return token;
	}

}
