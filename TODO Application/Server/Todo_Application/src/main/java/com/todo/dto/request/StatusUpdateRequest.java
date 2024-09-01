package com.todo.dto.request;

import com.todo.enums.TaskStatus;

public class StatusUpdateRequest {
	 private TaskStatus status;

     public TaskStatus getStatus() {
         return status;
     }

     public void setStatus(TaskStatus status) {
         this.status = status;
     }
}
