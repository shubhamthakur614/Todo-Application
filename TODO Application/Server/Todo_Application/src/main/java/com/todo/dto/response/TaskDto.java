package com.todo.dto.response;

import java.util.Date;

import com.todo.enums.TaskStatus;

public class TaskDto {

	private Long id;

	private String title;

	private String description;

	private Date dueDate;

	private String priority;

	private Long empId;

	private String employeeName;

	private TaskStatus taskStatus;

	public TaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskDto(Long id, String title, String description, Date dueDate, String priority, Long empId,
			String employeeName, TaskStatus taskStatus) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.empId = empId;
		this.employeeName = employeeName;
		this.taskStatus = taskStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

}
