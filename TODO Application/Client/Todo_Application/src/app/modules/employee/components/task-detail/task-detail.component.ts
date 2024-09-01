import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EmployeeService } from '../../services/employee.service';
import { TaskStatus } from '../../interfaces/task-status';
import { CommentRequest } from '../../../../auth/interfaces/comment-request';
import { CommentResponse } from '../../../../auth/interfaces/comment-response';
import { StorageService } from '../../../../auth/services/storage/storage.service';
import { NavbarComponent } from "../../../../../shared/components/navbar/navbar.component";


@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink, NavbarComponent],
  templateUrl: './task-detail.component.html',
  styleUrl: './task-detail.component.css'
})
export class TaskDetailComponent implements OnInit {
  task: any = {}; // Initialize task to an empty object
  statuses = Object.values(TaskStatus) // Enum values as strings
  id!: number; // Use the non-null assertion operator or initialize with a default value

  commentForm!: FormGroup;
  commentsList: CommentResponse[] = [];
  commentRequest: CommentRequest = {
    userId: 0,
    content: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private employeeService: EmployeeService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    const taskIdString = this.route.snapshot.paramMap.get('id');
    if (taskIdString === null) {
      console.error('Task ID is missing');
      return;
    }

    const taskId = +taskIdString; // Convert the ID to a number
    if (isNaN(taskId)) {
      console.error('Invalid task ID');
      return;
    }
    this.id = taskId;

    this.getTaskByTaskId();

    this.commentForm = this.fb.group({
      content: [null, Validators.required]
    });
    this.getAllComments();
  }

  getTaskByTaskId(): void {
    this.employeeService.getTaskByTaskId(this.id).subscribe({
      next: (response) => {
        this.task = response;
        console.log('Task fetched successfully:', response);
      },
      error: (err) => {
        console.error('Error fetching task:', err);
      }
    });
  }

  updateStatus(): void {
    if (!this.task || !this.task.taskStatus) {
      console.error('Task or status is undefined');
      return;
    }

    // Call the service to update the task status
    this.employeeService.updateTaskStatus(this.id, this.task.taskStatus).subscribe({
      next: (response) => {
        console.log(this.task.taskStatus)
        console.log('Status successfully updated:', response);
        this.task = response;
      },
      error: (err) => {
        console.error('Error updating status:', err);
      }
    });
  }

  //publish the comments
  publishComment(): void {
    if (this.commentForm.invalid) {
      console.error('Form is invalid');
      return;
    }

    // Assign form values to commentRequest
    this.commentRequest.content = this.commentForm.get('content')?.value;
    this.commentRequest.userId = StorageService.getUserId();

    console.log('Publishing Comment:', this.commentRequest);

    // Call the service to publish the comment
    this.employeeService.publishComment(this.id, this.commentRequest).subscribe({
      next: (response) => {
        console.log('Comment published successfully:', response);
        // Optionally reset the form
        this.commentForm.reset();
        this.getAllComments();
      },
      error: (err) => {
        console.error('Error publishing comment:', err);
      }
    });
  }
  // get All comments by Task Id
  getAllComments() {
    this.employeeService.getAllCommentsByTaskId(this.id).subscribe((response) => {
      console.log(response);
      this.commentsList = response;
    })
  }

  getCommentClass(comment: any): string {
  if (comment.postedBy === 'Current User') {
    return 'current-user-comment';
  }
  return comment.type === 'admin' ? 'admin-comment' : 'user-comment';
}

getIconClass(comment: any): string {
  if (comment.postedBy === 'Current User') {
    return 'fas fa-user-circle current-user-icon';
  }
  return comment.type === 'admin' ? 'fas fa-user-shield admin-icon' : 'fas fa-user user-icon';
}

getReplyClass(reply: any): string {
  if (reply.postedBy === 'Current User') {
    return 'current-user-comment';
  }
  return reply.type === 'admin' ? 'admin-comment' : 'user-comment';
}

getReplyIconClass(reply: any): string {
  if (reply.postedBy === 'Current User') {
    return 'fas fa-user-circle current-user-icon';
  }
  return reply.type === 'admin' ? 'fas fa-user-shield admin-icon' : 'fas fa-user user-icon';
}


  goBack(): void {
    this.router.navigate(['/employee/dashboard']);
  }
}