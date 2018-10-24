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

  constructor(private http: HttpClient) { }

  getTasksByStatus(status: string): Observable<Task[]> {
    let serviceUrl = `http://${environment.server}/api/tasks`;
    if (status === 'pending') serviceUrl += '?pending=true';
    else if (status === 'done') serviceUrl += '?pending=false';
    return this.http.get<Task[]>(serviceUrl);
  }

  getImageUrl(imageName: string): string {
    return `http://${environment.server}/files/${imageName}`;
  }
}
