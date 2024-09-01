import { Component, contentChild, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TaskStatus } from '../../../employee/interfaces/task-status';
import { EmployeeService } from '../../../employee/services/employee.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { StorageService } from '../../../../auth/services/storage/storage.service';
import { CommentRequest } from '../../../../auth/interfaces/comment-request';
import { CommentResponse } from '../../../../auth/interfaces/comment-response';
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
  statuses = Object.values(TaskStatus); // Enum values as strings
  id!: number; // Use the non-null assertion operator or initialize with a default value
  statusLevel: number = 1; // Default status level

  commentForm!: FormGroup;
  commentsList: CommentResponse []=[];
  commentRequest: CommentRequest = {
    userId: 0,
    content: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private adminService: AdminService,
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
        this.updateStatusLevel();
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
        console.log(this.task.taskStatus);
        console.log('Status successfully updated:', response);
        this.task = response;
        this.updateStatusLevel();
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
    this.adminService.publishComment(this.id, this.commentRequest).subscribe({
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
   getAllComments(){
    this.adminService.getAllCommentsByTaskId(this.id).subscribe((response)=>{
      console.log(response);
      this.commentsList=response;
    })
   }


  updateStatusLevel(): void {
    switch (this.task.taskStatus.toUpperCase()) {
      case 'ASSIGN':
        this.statusLevel = 1;
        break;
      case 'INPROGRESS':
        this.statusLevel = 2;
        break;
      case 'PENDING':
        this.statusLevel = 3;
        break;
      case 'COMPLETED':
        this.statusLevel = 4;
        break;
      default:
        this.statusLevel = 5;
        break;
    }
  }

  goBack(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  
}