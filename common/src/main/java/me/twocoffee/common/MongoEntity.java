package me.twocoffee.common;

import org.bson.types.*;

import com.google.code.morphia.annotations.*;

public abstract class MongoEntity {
	/**
	 * 
	 */
	@Id
	protected ObjectId _id;

	public ObjectId get_id() {
		return _id;
	}

	public String getId() {
		return (_id == null ? null : _id.toString());
	}

	public void set_id(ObjectId id) {
		_id = id;
	}

	public void setId(String id) {
		_id = new ObjectId(id);
	}

}
