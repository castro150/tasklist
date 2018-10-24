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

  onEnter(newTaskName): void {
    console.warn('NOVA', newTaskName);
  }

  switchFilter(newFilter): void {
    this.getTasks(newFilter);
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
