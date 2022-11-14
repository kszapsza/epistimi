import { GradeResponse } from './grade';

export interface StudentGradesResponse {
  id: string;
  firstName: string;
  lastName: string;
  subjects: StudentGradesSubjectResponse;
}

export interface StudentGradesSubjectResponse {
  id: string;
  name: string;
  firstSemester: StudentGradesSubjectSemesterResponse;
  secondSemester: StudentGradesSubjectSemesterResponse;
  average: StudentGradesAverageResponse;
}

export interface StudentGradesSubjectSemesterResponse {
  grades: GradeResponse[];
  average: StudentGradesAverageResponse;
}

export interface StudentGradesAverageResponse {
  student?: string;
  subject?: string;
}