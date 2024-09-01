import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { authGuard } from '../../auth.guard';

const routes: Routes = [
  {path:"dashboard",component:DashboardComponent,title:"Dashboard",canActivate:[authGuard]},
  {path:"taskdetails/:id",loadComponent:()=>import("./components/task-detail/task-detail.component").then((m)=>m.TaskDetailComponent)},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeRoutingModule { }
