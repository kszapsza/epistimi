import { ClassificationGradeTeacherResponse, ClassificationGradeValueResponse } from './classification-grade';
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
  classification: SubjectStudentClassificationResponse;
}

export interface SubjectGradesStudentSemesterResponse {
  grades: GradeResponse[];
  average?: string;
}

export interface SubjectStudentClassificationResponse {
  proposal?: SubjectStudentClassificationGradeResponse;
  final?: SubjectStudentClassificationGradeResponse;
}

export interface SubjectStudentClassificationGradeResponse {
  id: string;
  issuedBy: ClassificationGradeTeacherResponse;
  issuedAt: Date;
  updatedAt?: Date;
  value: ClassificationGradeValueResponse;
}

export interface SubjectGradesAverageResponse {
  firstSemester?: string;
  secondSemester?: string;
  overall?: string;
}