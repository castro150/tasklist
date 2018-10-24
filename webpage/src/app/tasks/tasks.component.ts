import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { TasksService } from '../services/tasks.service';
import { Task } from '../entity/task';

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  model = {
    name: ''
  };

  tasks: Task[];

  constructor(private tasksService: TasksService, private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.getTasks('all');
  }

  onEnter(newTaskName: string): void {
    this.spinner.show();
    this.tasksService.createNewTask(newTaskName)
      .subscribe(_ => {
        this.spinner.hide();
        this.model.name = '';
        this.getTasks('all');
      });
  }

  switchStatus(status: string): void {
    this.getTasks(status);
  }

  getTasks(status: string): void {
    this.spinner.show();
    this.tasksService.getTasksByStatus(status)
      .subscribe(tasks => {
        this.spinner.hide();
        this.tasks = tasks;
      });
  }

  getImageUrl(imageName: string): string {
    return this.tasksService.getImageUrl(imageName);
  }

}
