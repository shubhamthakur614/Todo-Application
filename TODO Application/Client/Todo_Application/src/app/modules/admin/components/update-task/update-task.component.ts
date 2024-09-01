import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { SignupResponse } from '../../../../auth/interfaces/signup-response';
import { TaskResponse } from '../../interfaces/task-response';
import { AdminService } from '../../services/admin.service';
import { NavbarComponent } from "../../../../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-update-task',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule, ReactiveFormsModule, NavbarComponent],
  templateUrl: './update-task.component.html',
  styleUrl: './update-task.component.css'
})
export class UpdateTaskComponent implements OnInit {  
  postTaskForm!: FormGroup;
  listOfEmployees: SignupResponse[] = [];
  listOfPriorities: string[] = ['LOW', 'MEDIUM', 'HIGH'];
  listOfTaskStatus: string[] = ['ASSIGN', 'INPROGRESS', 'PENDING', 'COMPLETED'];

  notificationMessage: string | null = null;
  notificationClass: string = '';
  id?: number; 
  task?: TaskResponse; 

  constructor(
    private adminService: AdminService,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.postTaskForm = this.fb.group({
      empId: [null, Validators.required],
      title: ['', [Validators.required]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      dueDate: [null, [Validators.required]],
      priority: ["", [Validators.required]],
      taskStatus: ["", [Validators.required]]
    });

    // Get the 'id' parameter from the route snapshot
    const idParam = this.route.snapshot.paramMap.get('id');
    this.id = idParam ? Number(idParam) : undefined;

    if (this.id !== undefined) {
      this.getTaskById();
    } else {
      console.error('ID is not defined or invalid');
    }

    this.getUsers();
  }

  getUsers() {
    this.adminService.getUsers().subscribe((res) => {
      this.listOfEmployees = res;
    });
  }

  getTaskById(): void {
    if (this.id !== undefined) {
      this.adminService.getTaskById(this.id).subscribe(
        (response: TaskResponse) => {
          this.task = response;

          // Format the date to YYYY-MM-DD if necessary
          const formattedDueDate = this.formatDate(this.task.dueDate);

          // Ensure taskStatus is correctly assigned
          this.postTaskForm.patchValue({
            empId: this.task.empId,
            title: this.task.title,
            description: this.task.description,
            dueDate: formattedDueDate,
            priority: this.task.priority,
            taskStatus: this.task.taskStatus // Ensure this matches exactly with listOfTaskStatus values
          });
        },
        error => {
          console.error('Error fetching task:', error);
        }
      );
    } else {
      console.error('ID is not defined');
    }
  }

  formatDate(date: Date | string | undefined): string {
    if (date instanceof Date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Months are 0-based
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    } else if (typeof date === 'string') {
      return date;
    }
    return '';
  }

  updateTask() {
    if (this.postTaskForm.valid) {
      this.adminService.updateTask(this.id!, this.postTaskForm.value).subscribe({
        next: (response) => {
          console.log(response);
          this.showNotification("Task Updated Successfully", 'success');
          setTimeout(() => {
            this.router.navigate(['admin/dashboard']);
          }, 1000);
        },
        error: (error) => {
          this.showNotification("Something Went Wrong, Please Try Again!...", 'error');
          setTimeout(() => {
            this.postTaskForm.reset();
            this.router.navigate(['admin/task']);
          }, 1000); 
        }
      });
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