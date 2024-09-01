package com.todo.dto.util;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.response.CommentDto;
import com.todo.entity.Comment;

public class CommentUtil {
	public static Comment getcommentsEntity(CommentRequest commentRequest) {
		Comment comment = new Comment();
		comment.setContent(commentRequest.getContent());
		return comment;
	}

	public static CommentDto getcommentsDto(Comment comment) {
		CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		commentDto.setContent(comment.getContent());
		commentDto.setCreatedAt(comment.getCreatedAt());
		commentDto.setPostedBy(comment.getUser().getName());
		commentDto.setTaskId(comment.getTask().getId());
		commentDto.setUserId(comment.getUser().getId());
		return commentDto;

	}
}
