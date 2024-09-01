package com.todo.service.employee;

import java.util.List;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.enums.TaskStatus;

public interface EmployeeService {

	List<TaskDto> getTaskByEmpId(Long id);
	
	TaskDto findByTaskId(Long id);

	TaskDto updateTaskStatus(Long taskId, TaskStatus status);
	
    CommentDto createComment(Long tid,CommentRequest commentRequest);
	
	List<CommentDto> getCommentsByTaskId(Long taskId);
}
