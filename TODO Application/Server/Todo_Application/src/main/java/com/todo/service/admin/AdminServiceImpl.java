package com.todo.service.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.dto.response.UserDto;
import com.todo.dto.util.CommentUtil;
import com.todo.dto.util.TaskUtil;
import com.todo.dto.util.UserUtil;
import com.todo.entity.Comment;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.enums.TaskStatus;
import com.todo.enums.UserRole;
import com.todo.exception.TaskNotFoundException;
import com.todo.repository.CommentRepository;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private CommentRepository commentRepository;


	@Override
	public TaskDto createTask(TaskRequest taskRequest) {
		Optional<User> optionalUser = userRepository.findById(taskRequest.getEmpId());
		if (optionalUser.isPresent()) {
			Task task = TaskUtil.getTaskEntity(taskRequest);
			task.setTaskStatus(TaskStatus.ASSIGN);
			task.setUser(optionalUser.get());
			Task savedTask = taskRepository.save(task);
			return TaskUtil.getTaskDto(savedTask);
		}
		return null;

	}
	@Override
	public List<UserDto> getUsers() {
		return userRepository.findAll()
				.stream()
				.filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
				.map(UserUtil::getUserDto)
				.collect(Collectors.toList());
	}

//	@Override
//	public List<TaskDto> getAllTasks() {
//		return taskRepository.findAll()
//				.stream()
//				.sorted(Comparator.comparing(Task::getDueDate)
//				.reversed())
//				.map(TaskUtil::getTaskDto).collect(Collectors.toList());
//	}
	
	@Override
	public Page<TaskDto> getAllTasks(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dueDate"));

	    Page<Task> tasksPage = taskRepository.findAll(pageable);
	    return tasksPage.map(TaskUtil::getTaskDto);
	}


	@Override
	public TaskDto getTaskById(Long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent()) {
			return optionalTask.map(TaskUtil::getTaskDto).orElseThrow(() -> new TaskNotFoundException(id));
		}
		throw new TaskNotFoundException(id);
	}

	@Override
	public TaskDto updateTask(Long id, TaskDto taskDto) {
	    Optional<Task> optionalTask = taskRepository.findById(id);
	    Optional<User> optionalUser = userRepository.findById(taskDto.getEmpId());

	    if (optionalTask.isPresent() && optionalUser.isPresent()) {
	        Task existingTask = optionalTask.get();

	        // Update only the fields that are not null in taskDto
	        if (taskDto.getTitle() != null) {
	            existingTask.setTitle(taskDto.getTitle());
	        }
	        if (taskDto.getDescription() != null) {
	            existingTask.setDescription(taskDto.getDescription());
	        }
	        if (taskDto.getDueDate() != null) {
	            existingTask.setDueDate(taskDto.getDueDate());
	        }
	        if (taskDto.getPriority() != null) {
	            existingTask.setPriority(taskDto.getPriority());
	        }
	        if (taskDto.getTaskStatus() != null) {
	            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDto.getTaskStatus())));
	        }
	        if (taskDto.getEmpId() != null) {
	            existingTask.setUser(optionalUser.get());
	        }

	        Task updatedTask = taskRepository.save(existingTask);
	        return TaskUtil.getTaskDto(updatedTask);
	    }

	    return null;
	}


	private TaskStatus mapStringToTaskStatus(String status) {
		return switch (status) {
		case "PENDING" -> TaskStatus.PENDING;
		case "INPROGRESS" -> TaskStatus.INPROGRESS;
		case "COMPLETED" -> TaskStatus.COMPLETED;
		case "DEFERED" -> TaskStatus.DEFERED;
		case "ASSIGN" -> TaskStatus.ASSIGN;
		default -> TaskStatus.CANCELD;
		};
	}

	
	@Override
	public List<TaskDto> searchTaskByTitle(String title) {
		return taskRepository.findByTitleContaining(title)
				.stream().sorted(Comparator.comparing(Task::getDueDate).reversed())
				.map(TaskUtil::getTaskDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public CommentDto createComment(Long tid,CommentRequest commentRequest) {
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
		return allComments.stream()
				.map(CommentUtil::getcommentsDto)
				.collect(Collectors.toList());
	}
	
	@Override
	public void deleteTask(Long id) {
		taskRepository.deleteById(id);

	}
	
}
