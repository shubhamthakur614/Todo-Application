package com.todo.dto.util;

import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.TaskDto;
import com.todo.entity.Task;

public class TaskUtil {

	public static Task getTaskEntity(TaskRequest taskRequest) {
		Task task = new Task();
		task.setTitle(taskRequest.getTitle());
		task.setDescription(taskRequest.getDescription());
		task.setDueDate(taskRequest.getDueDate());
		task.setPriority(taskRequest.getPriority());
		return task;
	}

	public static TaskDto getTaskDto(Task task) {
		TaskDto taskDto = new TaskDto();
		taskDto.setId(task.getId());
		taskDto.setTitle(task.getTitle());
		taskDto.setDescription(task.getDescription());
		taskDto.setDueDate(task.getDueDate());
		taskDto.setPriority(task.getPriority());
		taskDto.setEmployeeName(task.getUser().getName());
		taskDto.setEmpId(task.getUser().getId());
		taskDto.setTaskStatus(task.getTaskStatus());
		return taskDto;

	}
}
