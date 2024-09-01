import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PostTaskComponent } from './components/post-task/post-task.component';
import { UpdateTaskComponent } from './components/update-task/update-task.component';
import { TaskDetailComponent } from './components/task-detail/task-detail.component';

const routes: Routes = [
  {path:"dashboard",component:DashboardComponent,title:"Dashboard"},
  {path:"task",component:PostTaskComponent,title:"Task"},
  {path:"task/:id/edit",component:UpdateTaskComponent,title:"Edit Task"},
  {path:"taskdetail/:id",component:TaskDetailComponent,title:"Task Detail"},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
