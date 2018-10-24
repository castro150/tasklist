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

  getTasks(filter): Observable<Task[]> {
    console.warn('BUSCANDO TASKS', filter); // TODO apply filter
    return this.http.get<Task[]>(`http://${environment.server}/api/tasks`);
  }

  getImageUrl(imageName: string): string {
    return `http://${environment.server}/files/${imageName}`;
  }
}
