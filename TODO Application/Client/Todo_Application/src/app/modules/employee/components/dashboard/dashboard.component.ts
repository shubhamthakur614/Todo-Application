import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { StorageService } from '../../../../auth/services/storage/storage.service';
import { TaskResponse } from '../../../admin/interfaces/task-response';
import { EmployeeService } from '../../services/employee.service';
import { NavbarComponent } from "../../../../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  empId: any;
  taskList: TaskResponse[] = [];

  constructor(private router: Router, private employeeService: EmployeeService) {
    this.empId = StorageService.getUserId();

    this.getEmployeeTask();

  }

  getEmployeeTask() {
    this.employeeService.getEmployeeTasksById(this.empId).subscribe((response => {
      this.taskList = response;
    }))
  }

  getPriorityClass(priority: 'High' | 'Medium' | 'Low'): string {
    switch (priority) {
      case 'High':
        return 'priority-high';
      case 'Medium':
        return 'priority-medium';
      case 'Low':
        return 'priority-low';
      default:
        return '';
    }
  }

  getStatusClass(taskStatus: 'ASSIGN' | 'PENDING' | 'INPROGRESS' | 'COMPLETED' | 'DEFERRED' | 'CANCELLED'): string {
    switch (taskStatus) {
      case 'ASSIGN':
        return 'status-assign';
      case 'PENDING':
        return 'status-pending';
      case 'INPROGRESS':
        return 'status-in-progress';
      case 'COMPLETED':
        return 'status-completed';
      case 'DEFERRED':
        return 'status-deferred';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return '';
    }
  }

}
