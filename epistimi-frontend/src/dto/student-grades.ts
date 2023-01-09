import { ClassificationGradeTeacherResponse, ClassificationGradeValueResponse } from './classification-grade';
import { GradeResponse } from './grade';

export interface StudentsGradesResponse {
  students: StudentGradesResponse[];
}

export interface StudentGradesResponse {
  id: string;
  firstName: string;
  lastName: string;
  subjects: StudentGradesSubjectResponse[];
}

export interface StudentGradesSubjectResponse {
  id: string;
  name: string;
  firstSemester: StudentGradesSubjectSemesterResponse;
  secondSemester: StudentGradesSubjectSemesterResponse;
  average: StudentGradesAverageResponse;
  classification: StudentSubjectClassificationResponse;
}

export interface StudentSubjectClassificationResponse {
  proposal?: StudentSubjectClassificationGradeResponse;
  final?: StudentSubjectClassificationGradeResponse;
}

export interface StudentSubjectClassificationGradeResponse {
  id: string;
  issuedBy: ClassificationGradeTeacherResponse;
  issuedAt: Date;
  updatedAt?: Date;
  value: ClassificationGradeValueResponse;
}

export interface StudentGradesSubjectSemesterResponse {
  grades: GradeResponse[];
  average: StudentGradesAverageResponse;
  classification: StudentSubjectClassificationResponse;
}

export interface StudentGradesAverageResponse {
  student?: string;
  subject?: string;
}