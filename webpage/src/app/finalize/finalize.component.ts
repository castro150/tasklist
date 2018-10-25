import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { TasksService } from '../services/tasks.service';
import { Task } from '../entity/task';

@Component({
  selector: 'app-finalize',
  templateUrl: './finalize.component.html',
  styleUrls: ['./finalize.component.css']
})
export class FinalizeComponent implements OnInit {
  model = {};

  tasks: Task[];

  selectedTask: Task;

  selectedFile: File;

  showConfirm: boolean = false;

  constructor(private tasksService: TasksService, private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.getTasks('pending');
    setInterval(() => this.updateTasks(), 10000);
  }

  onFileChanged(event) {
    this.selectedFile = event.target.files[0];
  }

  doTask(task: Task) {
    this.selectedTask = task;
  }

  confirmFinalize(): void {
    this.spinner.show();
    this.tasksService.finalizeTask(this.selectedTask, this.selectedFile)
      .subscribe(() => {
        this.spinner.hide();
        this.selectedTask = null;
        this.selectedFile = null;
        this.showConfirm = false;
        this.getTasks('pending');
      });
  }

  cancelFinalize(): void {
    this.selectedTask = null;
    this.selectedFile = null;
    this.showConfirm = false;
  }

  getTasks(status: string): void {
    this.spinner.show();
    this.tasksService.getTasksByStatus(status)
      .subscribe(tasks => {
        this.spinner.hide();
        this.tasks = tasks;
      });
  }

  updateTasks(): void {
    this.tasksService.getTasksByStatus('pending')
      .subscribe(tasks => this.tasks = tasks);
  }

}
