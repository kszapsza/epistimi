import { StudentResponse } from './student';
import { TeacherResponse } from './teacher';

export interface CourseResponse {
  id: string;
  code: Code;
  schoolYear: string;
  classTeacher: TeacherResponse;
  students: StudentResponse[];
  schoolYearBegin: Date;
  schoolYearSemesterEnd: Date;
  schoolYearEnd: Date;
  profile?: string;
  profession?: string;
  specialization?: string;
}

export interface Code {
  number: string;
  letter: string;
}

export interface CoursesResponse {
  courses: CourseResponse[];
}

export interface CourseCreateRequest {
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
