import { UserResponse } from './user';

export interface TeacherResponse {
  id: string;
  user: UserResponse;
  academicTitle?: string;
}

export interface TeachersResponse {
  teachers: TeacherResponse[];
}
