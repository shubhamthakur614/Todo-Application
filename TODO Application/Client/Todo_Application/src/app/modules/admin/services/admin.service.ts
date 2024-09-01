import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { StorageService } from '../../../auth/services/storage/storage.service';
import { CommentRequest } from '../../../auth/interfaces/comment-request';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  URL = environment.BASE_URL;

  constructor(private http: HttpClient) { }

  //get Users 
  getUsers(): Observable<any> {
    return this.http.get(`${this.URL}api/admin/users`, {
      headers: this.createAuthorizationHeader()
    })
  }
  //Get Task By Id
  getTaskById(id: number): Observable<any> {
    return this.http.get(`${this.URL}api/admin/task/` + id, {
      headers: this.createAuthorizationHeader()
    })
  }

  //get All Task
  // getAllTasks(): Observable<any> {
  //   return this.http.get(`${this.URL}api/admin/tasks`, {
  //     headers: this.createAuthorizationHeader()
  //   })
  // }

  getAllTasks(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.URL}api/admin/tasks?page=${page}&size=${size}`, 
      { headers: this.createAuthorizationHeader() })
  }

  //post task
  postTask(taskRequest: any): Observable<any> {
    return this.http.post(`${this.URL}api/admin/task`, taskRequest, {
      headers: this.createAuthorizationHeader()
    })
  }

  //Update task
  updateTask(id: number, taskRequest: any): Observable<any> {
    return this.http.put(`${this.URL}api/admin/task/${id}`, taskRequest, {
      headers: this.createAuthorizationHeader()
    })
  }

  //publish comment
  publishComment(taskId: number, commentRequest: CommentRequest): Observable<any> {
    // Set headers to include 'Content-Type: application/json'
    const headers = this.createAuthorizationHeader().set('Content-Type', 'application/json');
    // Convert commentRequest object to JSON string
    const body = JSON.stringify(commentRequest);
    console.log("Request Body:", body);
    // Send POST request with the JSON body
    return this.http.post(`${this.URL}api/admin/task/${taskId}/comment`, body, { headers });
  }

  //get Comments by Task Id
  getAllCommentsByTaskId(taskId: number): Observable<any> {
    return this.http.get(`${this.URL}api/admin/task/comments/${taskId}`, {
      headers: this.createAuthorizationHeader()
    })
  }

  //delete task by Id
  deleteTask(id: number): Observable<any> {
    return this.http.delete(`${this.URL}api/admin/task/` + id, {
      headers: this.createAuthorizationHeader()
    })
  }


  //get the jwt token from local storage
  private createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + StorageService.getToken()
    )
  }
}
