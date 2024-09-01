export enum UserRole {
    ADMIN = 'ADMIN',
    EMPLOYEE = 'EMPLOYEE',
}
export interface SignupResponse {
    id: number;          // TypeScript uses number for all numeric values
    name: string;
    email: string;
    password: string;   // Be cautious with sensitive data like passwords
    userRole: UserRole; // Use the enum if applicable
}
