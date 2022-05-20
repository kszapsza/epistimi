import { UserRegisterRequest, UserResponse } from './user';

export interface TeacherResponse {
  id: string;
  user: UserResponse;
  academicTitle?: string;
}

export interface TeachersResponse {
  teachers: TeacherResponse[];
}

export interface TeacherRegisterRequest {
  user: UserRegisterRequest,
  academicTitle?: string,
}

export interface TeacherRegisterResponse {
  id: string,
  newUser: {
    user: UserResponse,
    password: string,
  },
  academicTitle?: string,
}
