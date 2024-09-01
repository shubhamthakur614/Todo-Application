import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SignupResponse } from '../../../../auth/interfaces/signup-response';
import { AdminService } from '../../services/admin.service';
import { NavbarComponent } from "../../../../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-post-task',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './post-task.component.html',
  styleUrl: './post-task.component.css'
})
export class PostTaskComponent {
  postTaskForm!: FormGroup;

  listOfEmployees:SignupResponse[] = [];
  listOfPriorities: any = ['LOW', 'MEDIUM', 'HIGH'];

  notificationMessage: string | null = null;
  notificationClass: string = '';

  constructor(private adminService: AdminService, private fb: FormBuilder, private router: Router) {
    this.getUsers();
  }

  ngOnInit() {
    this.postTaskForm = this.fb.group({
      empId: [null, Validators.required],
      title: ['', [Validators.required,]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      dueDate: [null, [Validators.required]],
      priority: ["", [Validators.required]]
    });
  }

  getUsers() {
    this.adminService.getUsers().subscribe((res) => {
      // console.log(res);
      this.listOfEmployees = res;
    })
  }
  postTask() {
    if (this.postTaskForm.valid) {
      this.adminService.postTask(this.postTaskForm.value).subscribe({
        next: (respose) => {
          console.log(respose);
          this.showNotification("Task Created Successfully", 'success');
          setTimeout(() => {
            this.router.navigate(['admin/dashboard']);
          }, 1000);
        },
        error: (error) => {
          this.showNotification("Something Went Wrong, Please Try Again!...", 'error');
          setTimeout(() => {
            this.postTaskForm.reset();
            this.router.navigate(['admin/task']);
          }, 1000); // Show notification for 2 seconds before redirecting
        }
      })
    }
  }

  showNotification(message: string, type: 'success' | 'error') {
    this.notificationMessage = message;
    this.notificationClass = `notification ${type}`;
    setTimeout(() => {
      this.notificationMessage = null;
      this.notificationClass = '';
    }, 1000);
  }
}
