import { Component, OnInit } from '@angular/core';
import { TasksService } from '../services/tasks.service'
import { Task } from '../entity/task'

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  model = {
    name: ''
  }

  tasks: Task[]

  constructor(private tasksService: TasksService) { }

  ngOnInit() {
    this.getTasks('all')
  }

  onEnter(newTaskName): void {
    console.warn('NOVA', newTaskName);
  }

  switchFilter(newFilter): void {
    console.warn('ENTROU', newFilter);
    this.getTasks(newFilter)
  }

  getTasks(filter): void {
    this.tasksService.getTasks(filter)
      .subscribe(tasks => this.tasks = tasks);
  }

}
