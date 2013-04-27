package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;

import me.twocoffee.dao.TokenDao;
import me.twocoffee.entity.Token;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author lisong
 */
public class TokenDaoImpl extends BasicDAO<Token, String> implements TokenDao {

	private static final String ID = "_id";
	private static final String COLLECTION_NAME = "token";
	private static final String REFER = "refer";
	private static final String CREATE_TIME = "createTime";

	protected TokenDaoImpl(Datastore ds) {
		super(ds);
		ensureCompoundIndex();
	}

	private void ensureCompoundIndex() {
		DBObject index = new BasicDBObject().append(CREATE_TIME, -1).append(
				REFER, 1);
		DBObject options = new BasicDBObject().append("background", true);
		getDatastore().getDB().getCollection(COLLECTION_NAME).ensureIndex(
				index,
				options);
	}

	@Override
	public void createToken(Token token) {

		if (token.getCreateTime() == null) {
			token.setCreateTime(new Date());
		}

		getDatastore().save(token);

	}

	@Override
	public Token findById(String id) {
		return getDatastore().createQuery(Token.class).filter(ID, id).get();
	}

	@Override
	public List<Token> listByReferAndCreateTime(String refer,
			Date minCreateTime) {
		return createQuery().filter(
				REFER, refer).filter(CREATE_TIME + " >", minCreateTime)
				.order("-" + CREATE_TIME).asList();
	}

	@Override
	public Token findLastTokenByRefer(String refer) {
		return createQuery().filter(REFER, refer).order("-" + CREATE_TIME)
				.get();
	}

}
