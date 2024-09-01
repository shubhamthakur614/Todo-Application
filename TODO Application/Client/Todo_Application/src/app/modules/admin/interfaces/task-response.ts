export type TaskStatus = 'ASSIGN' | 'INPROGRESS' | 'PENDING' | 'COMPLETED' | 'DEFERRED' | 'CANCELLED';

export interface TaskResponse {
    id: number,

    title: string,

    description: string,

    dueDate: Date,

    priority: 'High' | 'Medium' | 'Low',

    empId: number,

    employeeName: string;

    taskStatus: TaskStatus;
}
