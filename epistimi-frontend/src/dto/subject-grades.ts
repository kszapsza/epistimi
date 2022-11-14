import { GradeResponse } from './grade';

export interface SubjectGradesResponse {
  id: string;
  name: string;
  students: SubjectGradesStudentResponse[];
  average: SubjectGradesAverageResponse;
}

export interface SubjectGradesStudentResponse {
  id: string;
  firstName: string;
  lastName: string;
  firstSemester: SubjectGradesStudentSemesterResponse;
  secondSemester: SubjectGradesStudentSemesterResponse;
  average?: string;
}

export interface SubjectGradesStudentSemesterResponse {
  grades: GradeResponse[];
  average?: string;
}

export interface SubjectGradesAverageResponse {
  firstSemester?: string;
  secondSemester?: string;
  overall?: string;
}