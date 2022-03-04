export interface UserResponse {
  id: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  username: string;
}

export enum UserRole {
  EPISTIMI_ADMIN = 'EPISTIMI_ADMIN',
  ORGANIZATION_ADMIN = 'ORGANIZATION_ADMIN',
  TEACHER = 'TEACHER',
  STUDENT = 'STUDENT',
  PARENT = 'PARENT',
}
