package me.twocoffee.rest.entity;

import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Settings.FileUploadSettings;
import me.twocoffee.entity.Settings.Sharing;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
// TODO:SNS add authority
public class DeviceSetting {
	public static class Aps {
		private boolean alert;

		public boolean isAlert() {
			return alert;
		}

		public void setAlert(boolean alert) {
			this.alert = alert;
		}

	}

	public static class Authority {

		private String name;

		private String avatar;

		private boolean synchronous;

		private String uid;

		public String getAvatar() {
			return avatar;
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public boolean isSynchronous() {
			return synchronous;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setSynchronous(boolean synchronous) {
			this.synchronous = synchronous;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

	}

	public static class MailDrop {
		public enum AddressVisibility {
			Public,
			Friend

		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Friend {
			private String name;
			private List<String> address;

			public List<String> getAddress() {
				return address;
			}

			public String getName() {
				return name;
			}

			public void setAddress(List<String> address) {
				this.address = address;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Personal {
			@JsonIgnoreProperties(ignoreUnknown = true)
			public static class Setting {
				private List<String> autoTag;

				public List<String> getAutoTag() {
					return autoTag;
				}

				public void setAutoTag(List<String> autoTag) {
					this.autoTag = autoTag;
				}
			}

			private Setting setting;
			private List<String> address;

			public List<String> getAddress() {
				return address;
			}

			public Setting getSetting() {
				return setting;
			}

			public void setAddress(List<String> address) {
				this.address = address;
			}

			public void setSetting(Setting setting) {
				this.setting = setting;
			}
		}

		private AddressVisibility addressVisibility;
		private List<String> coffeeFriend;

		private List<Friend> friend;

		private Personal personal;

		public AddressVisibility getAddressVisibility() {
			return addressVisibility;
		}

		public List<String> getCoffeeFriend() {
			return coffeeFriend;
		}

		public List<Friend> getFriend() {
			return friend;
		}

		public Personal getPersonal() {
			return personal;
		}

		public void setAddressVisibility(AddressVisibility addressVisibility) {
			this.addressVisibility = addressVisibility;
		}

		public void setCoffeeFriend(List<String> coffeeFriend) {
			this.coffeeFriend = coffeeFriend;
		}

		public void setFriend(List<Friend> friend) {
			this.friend = friend;
		}

		public void setPersonal(Personal personal) {
			this.personal = personal;
		}
	}

	private Aps aps;

	private MailDrop mailDrop;

	private Map<ThirdPartyType, Authority> authority;

	private Sharing sharing;

	private FileUploadSettings fileupload;

	public Aps getAps() {
		return aps;
	}

	public Map<ThirdPartyType, Authority> getAuthority() {
		return authority;
	}

	public FileUploadSettings getFileupload() {
		return fileupload;
	}

	public MailDrop getMailDrop() {
		return mailDrop;
	}

	public Sharing getSharing() {
		return sharing;
	}

	public void setAps(Aps aps) {
		this.aps = aps;
	}

	public void setAuthority(Map<ThirdPartyType, Authority> authority) {
		this.authority = authority;
	}

	public void setFileupload(FileUploadSettings fileupload) {
		this.fileupload = fileupload;
	}

	public void setMailDrop(MailDrop mailDrop) {
		this.mailDrop = mailDrop;
	}

	public void setSharing(Sharing sharing) {
		this.sharing = sharing;
	}

}
