package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value = "tag", noClassnameStored = true)
public class Tag {
	public static class TagId {
		private String accountId;// 账户id
		private String name;// tag名称

		public TagId() {
		}

		public TagId(String accountId, String name) {
			this.accountId = accountId;
			this.name = name;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TagId) {
				TagId right = (TagId) obj;
				return (this.accountId + this.name)
						.equals(right.accountId + right.name);
			} else {
				return super.equals(obj);
			}
		}

		public String getAccountId() {
			return accountId;
		}

		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			return (this.accountId + this.name)
					.hashCode();
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private int counter;// 计数器：这个tag被该用户标记了多少次
	private Date date;// 创建时间

	@Id
	private TagId id;

	public int getCounter() {
		return counter;
	}

	public Date getDate() {
		return date;
	}

	public TagId getId() {
		return id;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(TagId id) {
		this.id = id;
	}
}
