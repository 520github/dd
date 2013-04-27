package me.twocoffee.common;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;

public abstract class BaseDao<T> {
	protected Datastore dataStore = null;
	protected Class<T> entityClass = null;

	@SuppressWarnings("unchecked")
	public BaseDao() {
		dataStore = (Datastore) SpringContext.getBean("datastore");
		setEntityClass();
	}

	protected void setEntityClass() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private Object changeObject(Object instance, String key, Object object)
			throws Exception {
		Method[] methods = instance.getClass().getMethods();
		String methodName = "set" + key;
		Method setMethod = findMethod(methods, methodName);
		try {
			setMethod.invoke(instance, object);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return instance;
	}

	private Method findMethod(Method[] methods, String methodName) {
		for (Method m : methods) {
			if (m.getName().toLowerCase().equals(methodName.toLowerCase())) {
				return m;
			}
		}
		return null;
	}

	protected Query<T> createQuery() {
		return this.dataStore.createQuery(entityClass);
	}

	public void delete(String id) {
		Query<T> q = this.dataStore.createQuery(entityClass).filter("id =", id);
		this.dataStore.delete(q);
	}

	public T getById(String id) {
		Query<T> q = this.dataStore.createQuery(entityClass).filter("id =", id);
		return q.get();
	}

	/**
	 * 批量保存实体bean。
	 * 
	 * @param entityList
	 */
	public void save(List<T> entityList) {
		dataStore.save(entityList);
	}

	public void save(T t) {
		dataStore.save(t);
	}

	public String saveAndReturnId(T t) {
		Key<T> keyObj = dataStore.save(t);
		return keyObj.getId() == null ? "" : keyObj.getId().toString();
	}

	public boolean update(Key<T> id, Map<String, Object> updateMap) {
		if (updateMap == null || updateMap.size() < 1) {
			return false;
		}

		Query<T> q = dataStore.createQuery(entityClass).filter("id =", id);
		Object instance = q.get();
		for (String key : updateMap.keySet()) {
			try {
				instance = changeObject(instance, key, updateMap.get(key));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		Key<Object> k = dataStore.save(instance);
		return k.getId() != null;
	}
}
