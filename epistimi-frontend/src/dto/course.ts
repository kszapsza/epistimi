import { StudentResponse } from './student';
import { TeacherResponse } from './teacher';

export interface CourseResponse {
  id: string;
  code: CourseCode;
  schoolYear: string;
  classTeacher: TeacherResponse;
  students: StudentResponse[];
  subjects: CourseSubjectResponse[];
  schoolYearBegin: Date;
  schoolYearSemesterEnd: Date;
  schoolYearEnd: Date;
  profile?: string;
  profession?: string;
  specialization?: string;
}

export interface CourseCode {
  number: string;
  letter: string;
}

export interface CourseSubjectResponse {
  id: string;
  teacher: CourseSubjectTeacherResponse;
  name: string;
}

export interface CourseSubjectTeacherResponse {
  id: string;
  academicTitle: string | null;
  firstName: string;
  lastName: string;
  username: string;
}

export interface CoursesResponse {
  courses: CourseResponse[];
}

export interface CourseFormData {
  codeNumber?: number;
  codeLetter: string;
  schoolYearBegin?: Date;
  schoolYearSemesterEnd?: Date;
  schoolYearEnd?: Date;
  classTeacherId: string;
  profile?: string;
  profession?: string;
  specialization?: string;
}

export interface CourseCreateRequest {
  codeNumber?: number;
  codeLetter: string;
  schoolYearBegin?: string;
  schoolYearSemesterEnd?: string;
  schoolYearEnd?: string;
  classTeacherId: string;
  profile?: string;
  profession?: string;
  specialization?: string;
}
