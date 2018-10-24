import { Injectable } from '@angular/core'
import { Observable, of } from 'rxjs'

import { Task } from '../entity/task'

@Injectable({
  providedIn: 'root'
})
export class TasksService {

  constructor() { }

  getTasks(filter): Observable<Task[]> {
    let tasks: Task[] = [
      { id: 1, name: 'Task 1', pending: false, image: 'task-test.png' },
      { id: 2, name: 'Task 2', pending: true, image: '' },      
      { id: 1, name: 'Task 3', pending: false, image: '' },
    ]
    console.warn('BUSCANDO TASKS', filter)
    return of(tasks)
  }
}
