import { UserRole } from './user';

export interface SubjectResponse {
  id: string;
  course: SubjectCourseResponse;
  teacher: SubjectTeacherResponse;
  name: string;
}

export interface SubjectCourseResponse {
  id: string;
  code: string;
  schoolYear: string;
  classTeacher: SubjectTeacherResponse;
  students: SubjectStudentResponse[];
}

export interface SubjectTeacherResponse {
  id: string;
  academicTitle: string | null;
  user: SubjectUserResponse;
}

export interface SubjectUserResponse {
  id: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  username: string;
}

export interface SubjectStudentResponse {
  id: string;
  user: SubjectUserResponse;
}

export interface SubjectRegisterRequest {
  courseId: string;
  teacherId: string;
  name: string;
}
