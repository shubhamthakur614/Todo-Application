import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap, catchError, of } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { TaskResponse, TaskStatus } from '../../admin/interfaces/task-response';
import { StorageService } from '../../../auth/services/storage/storage.service';
import { CommentRequest } from '../../../auth/interfaces/comment-request';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private URL = environment.BASE_URL;
  taskList: TaskResponse[] = []; // Store tasks internally
  task?: TaskResponse;
  constructor(private http: HttpClient) { }

  // Get all Tasks Associated with Employee based on employeeId
  getEmployeeTasksById(id: number): Observable<TaskResponse[]> {
    return this.http.get<TaskResponse[]>(`${this.URL}api/employee/tasks/` + id, {
      headers: this.createAuthorizationHeader()
    }).pipe(
      tap(tasks => this.taskList = tasks),
      catchError(error => {
        console.error('Error fetching tasks', error);
        return of([]); // Return an empty array on error
      })
    );
  }

  // Get task by ID 
  getTaskByTaskId(id: number): Observable<TaskResponse> {
    return this.http.get<TaskResponse>(`${this.URL}api/employee/task/${id}`, {
      headers: this.createAuthorizationHeader()
    })
  }


  // Update task status
  updateTaskStatus(taskId: number, status: TaskStatus): Observable<any> {
    const headers = this.createAuthorizationHeader().set('Content-Type', 'application/json');
    // Create the request body object
    const body = {
      status: status // Assuming status is of type TaskStatus which matches backend enum
    };
    return this.http.put(`${this.URL}api/employee/task/${taskId}/status`, body, { headers });
  }

  //publish comment
  publishComment(taskId: number, commentRequest: CommentRequest): Observable<any> {
    // Set headers to include 'Content-Type: application/json'
    const headers = this.createAuthorizationHeader().set('Content-Type', 'application/json');
    // Convert commentRequest object to JSON string
    const body = JSON.stringify(commentRequest);
    // Send POST request with the JSON body
    return this.http.post(`${this.URL}api/employee/task/${taskId}/comment`, body, { headers });
  }

  //get Comments by Task Id
  getAllCommentsByTaskId(taskId: number): Observable<any> {
    return this.http.get(`${this.URL}api/employee/task/comments/${taskId}`, {
      headers: this.createAuthorizationHeader()
    })
  }

  //Get the JWT token from local storage
  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + StorageService.getToken()
    );
  }
}
