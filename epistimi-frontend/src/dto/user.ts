export interface UserResponse {
  id: String,
  firstName: String,
  lastName: String,
  role: UserRole,
  username: String,
}

export enum UserRole {
  EPISTIMI_ADMIN = 'EPISTIMI_ADMIN',
  ORGANIZATION_ADMIN = 'ORGANIZATION_ADMIN',
  TEACHER = 'TEACHER',
  STUDENT = 'STUDENT',
  PARENT = 'PARENT',
}
