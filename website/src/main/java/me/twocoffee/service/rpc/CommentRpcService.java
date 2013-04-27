package me.twocoffee.service.rpc;

import me.twocoffee.entity.Comment;

public interface CommentRpcService {
	boolean saveComment(String token, String contentId, Comment comment);
}
