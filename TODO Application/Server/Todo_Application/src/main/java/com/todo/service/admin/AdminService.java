package com.todo.service.admin;

import java.util.List;

import org.springframework.data.domain.Page;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.dto.response.UserDto;

public interface AdminService {

	List<UserDto> getUsers();

	TaskDto createTask(TaskRequest taskRequest);

//	List<TaskDto> getAllTasks();

	void deleteTask(Long id);
	
	TaskDto getTaskById(Long id); 
	
	TaskDto updateTask(Long id,TaskDto taskDto);
	
	List<TaskDto> searchTaskByTitle(String title);

	CommentDto createComment(Long tid,CommentRequest commentRequest);
	
	List<CommentDto> getCommentsByTaskId(Long taskId);

	Page<TaskDto> getAllTasks(int page, int size);

}
