package me.twocoffee.service.rpc;

public class ContentAlreadyCollectException extends Exception {
	private String contentId;

	public ContentAlreadyCollectException(String contentId) {
		this.contentId = contentId;
	}

	@Override
	public String getMessage() {
		return "content " + contentId + " already collect";
	}
}
