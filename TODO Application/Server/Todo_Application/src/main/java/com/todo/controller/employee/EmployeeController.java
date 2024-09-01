package com.todo.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.todo.dto.request.CommentRequest;
import com.todo.dto.request.StatusUpdateRequest;
import com.todo.dto.response.CommentDto;
import com.todo.dto.response.TaskDto;
import com.todo.service.employee.EmployeeService;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {

	 private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	 
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/tasks/{id}")
	public ResponseEntity<?> getTaskByUserId(@PathVariable("id") Long id) {
		List<TaskDto> taskByEmpId = employeeService.getTaskByEmpId(id);
		return new ResponseEntity<>(taskByEmpId, HttpStatus.OK);
	}
	
	@GetMapping("/task/{id}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") Long id) {
		TaskDto taskById = employeeService.findByTaskId(id);
		return new ResponseEntity<>(taskById, HttpStatus.OK);
	}
	
	@PutMapping("/task/{taskId}/status")
	public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long taskId,
			@RequestBody StatusUpdateRequest statusUpdateRequest) {
		TaskDto updatedTask = employeeService.updateTaskStatus(taskId, statusUpdateRequest.getStatus());
		if (updatedTask != null) {
			return ResponseEntity.ok(updatedTask);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/task/{id}/comment")
	public ResponseEntity<?> createComment(@PathVariable("id") Long id, @RequestBody CommentRequest commentRequest) {
		CommentDto commentDto = employeeService.createComment(id, commentRequest);
		if (commentDto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
	}

	@GetMapping("/task/comments/{taskId}")
	public ResponseEntity<?> getAllCommetsByTaskId(@PathVariable("taskId") Long taskId) {
		List<CommentDto> commentsByTaskId = employeeService.getCommentsByTaskId(taskId);
		return new ResponseEntity<>(commentsByTaskId, HttpStatus.OK);
	}

}
