package com.todo.dto.request;

import java.util.Date;

public class TaskRequest {

	private String title;

	private String description;

	private Date dueDate;

	private String priority;

	private Long empId;

	public TaskRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskRequest(String title, String description, Date dueDate, String priority, Long empId) {
		super();
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.priority = priority;
		this.empId = empId;
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

}
