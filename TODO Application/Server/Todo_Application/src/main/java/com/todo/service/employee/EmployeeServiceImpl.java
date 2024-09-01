package com.todo.service.employee;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.dto.util.CommentUtil;
import com.todo.dto.util.TaskUtil;
import com.todo.entity.Comment;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.enums.TaskStatus;
import com.todo.exception.TaskNotFoundException;
import com.todo.repository.CommentRepository;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	
	@Override
	public List<TaskDto> getTaskByEmpId(Long id) {
	    List<Task> tasks = taskRepository.findAllByUserId(id);
	    
	    // If tasks is null or empty, return an empty list
	    if (tasks == null || tasks.isEmpty()) {
	        return Collections.emptyList();
	    }
	    return tasks.stream()
	                .sorted(Comparator.comparing(Task::getDueDate).reversed())
	                .map(TaskUtil::getTaskDto)
	                .collect(Collectors.toList());
	}


	@Override
	public TaskDto findByTaskId(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent()) {
			return optionalTask.map(TaskUtil::getTaskDto).orElseThrow(() -> new TaskNotFoundException(id));
		}
		throw new TaskNotFoundException(id);
	}
	@Override
	public TaskDto updateTaskStatus(Long taskId, TaskStatus status) {
		return taskRepository.findById(taskId).map(task -> {
			task.setTaskStatus(status);
			Task updatedTask = taskRepository.save(task);
			return TaskUtil.getTaskDto(updatedTask);
		}).orElse(null);
	}


	@Override
	public CommentDto createComment(Long tid, CommentRequest commentRequest) {
		Optional<Task> optionalTask = taskRepository.findById(tid);
		Optional<User> optionalUser = userRepository.findById(commentRequest.getUserId());
		if (optionalTask != null && optionalUser != null) {
			Comment comment = CommentUtil.getcommentsEntity(commentRequest);
			comment.setCreatedAt(new Date());
			comment.setTask(optionalTask.get());
			comment.setUser(optionalUser.get());
			Comment commentDto = commentRepository.save(comment);
			return CommentUtil.getcommentsDto(commentDto);
		}
		throw new EntityNotFoundException("User or task Not Found...");
	}

	@Override
	public List<CommentDto> getCommentsByTaskId(Long taskId) {
		List<Comment> allComments = commentRepository.findAllByTaskId(taskId);
		return allComments.stream().map(CommentUtil::getcommentsDto).collect(Collectors.toList());
	}


}
