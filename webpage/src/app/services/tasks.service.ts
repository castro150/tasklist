import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Task } from '../entity/task';
import { environment } from '../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class TasksService {

  private tasksUrl = `http://${environment.server}/api/tasks`;

  constructor(private http: HttpClient) { }

  getTasksByStatus(status: string): Observable<Task[]> {
    let serviceUrl = this.tasksUrl;
    if (status === 'pending') serviceUrl += '?pending=true';
    else if (status === 'done') serviceUrl += '?pending=false';
    return this.http.get<Task[]>(serviceUrl);
  }

  createNewTask(newTaskName: string): Observable<Task> {
    let newTask: Task = {
      id: null,
      name: newTaskName,
      pending: true,
      image: null
    };
    return this.http.post<Task>(this.tasksUrl, newTask, httpOptions);
  }

  finalizeTask(task: Task, file: File): Observable<Object> {
    let formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.put(`${this.tasksUrl}/finalize/${task.id}`, formdata);
  }

  getImageUrl(imageName: string): string {
    return `http://${environment.server}/files/${imageName}`;
  }
}
