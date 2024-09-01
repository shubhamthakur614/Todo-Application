package com.todo.controller.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.controller.employee.EmployeeController;
import com.todo.dto.request.CommentRequest;
import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
	
	 private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	 
	@Autowired
	private AdminService adminService;

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(adminService.getUsers());
	}

	@PostMapping("/task")
	public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
		TaskDto createdTask = adminService.createTask(taskRequest);
		if (createdTask == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
	}

//	@GetMapping("/tasks")
//	public ResponseEntity<?> getAllTasks() {
//		return ResponseEntity.ok(adminService.getAllTasks());
//	}
	
	@GetMapping("/tasks")
	public ResponseEntity<?> getAllTasks(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    Page<TaskDto> pagedResult = adminService.getAllTasks(page, size);
	    return ResponseEntity.ok(pagedResult);
	}


	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") Long id) {
		TaskDto taskById = adminService.getTaskById(id);
		System.out.println(taskById);
		return new ResponseEntity<>(taskById, HttpStatus.OK);
	}

	@PutMapping("/task/{id}")
	public ResponseEntity<?> updateTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) {
		TaskDto updateTask = adminService.updateTask(id, taskDto);
		if (updateTask == null) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<>(updateTask, HttpStatus.OK);
	}

	@GetMapping("/tasks/search/{title}")
	public ResponseEntity<?> searchTask(@PathVariable("title") String title) {
		return new ResponseEntity<>(adminService.searchTaskByTitle(title), HttpStatus.OK);
	}

	@PostMapping("/task/{id}/comment")
	public ResponseEntity<?> createComment(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
		CommentDto commentDto = adminService.createComment(id, commentRequest);
		if (commentDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
	}

	@GetMapping("/task/comments/{taskId}")
	public ResponseEntity<?> getAllCommetsByTaskId(@PathVariable("taskId") Long taskId) {
		List<CommentDto> commentsByTaskId = adminService.getCommentsByTaskId(taskId);
		return new ResponseEntity<>(commentsByTaskId, HttpStatus.OK);
	}

	@DeleteMapping("/task/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
		adminService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

}
