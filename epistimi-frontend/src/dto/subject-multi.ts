import { UserRole } from './user';

export interface SubjectsResponse {
  schoolYears: SchoolYearSubjectsResponse[];
}

export interface SchoolYearSubjectsResponse {
  schoolYear: string;
  courses: CourseSubjectsResponse[];
}

export interface CourseSubjectsResponse {
  courseId: string;
  code: string;
  classTeacher: SubjectsTeacherResponse;
  studentsCount: number;
  subjects: SubjectsEntryResponse[];
}

export interface SubjectsTeacherResponse {
  id: string;
  academicTitle?: string;
  user: SubjectsUserResponse;
}

export interface SubjectsUserResponse {
  id: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  username: string;
}

export interface SubjectsEntryResponse {
  id: string;
  teacher: SubjectsTeacherResponse;
  name: string;
}