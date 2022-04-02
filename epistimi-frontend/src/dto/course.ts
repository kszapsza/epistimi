import { StudentResponse } from './student';
import { TeacherResponse } from './teacher';

export interface CourseResponse {
  id: string;
  code: Code;
  schoolYear: string;
  classTeacher: TeacherResponse;
  students: StudentResponse[];
}

export interface Code {
  number: number;
  letter: string;
}

export interface CoursesResponse {
  courses: CourseResponse[];
}
