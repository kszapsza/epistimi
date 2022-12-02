import { GradeValue } from './grade-values';

export const enum ClassificationGradePeriod {
  FIRST_SEMESTER = 'FIRST_SEMESTER',
  SECOND_SEMESTER = 'SECOND_SEMESTER',
  SCHOOL_YEAR = 'SCHOOL_YEAR',
}

export interface ClassificationGradeIssueRequest {
  subjectId: string;
  studentId: string;
  period: ClassificationGradePeriod,
  value: GradeValue,
  isProposal: boolean;
}

export interface ClassificationGradeResponse {
  id: string;
  subject: ClassificationGradeSubjectResponse;
  student: ClassificationGradeStudentResponse;
  issuedBy: ClassificationGradeTeacherResponse;
  issuedAt: Date;
  updatedAt?: Date;
  value: ClassificationGradeValueResponse;
  period: ClassificationGradePeriod;
  isProposal: boolean;
}

export interface ClassificationGradeSubjectResponse {
  id: string;
  name: string;
  course: ClassificationGradeCourseResponse;
}

export interface ClassificationGradeCourseResponse {
  id: string;
  code: string;
  schoolYear: string;
}

export interface ClassificationGradeStudentResponse {
  id: string;
  firstName: string;
  lastName: string;
}

export interface ClassificationGradeTeacherResponse {
  id: string;
  academicTitle?: string;
  firstName: string;
  lastName: string;
}

export interface ClassificationGradeValueResponse {
  displayName: string;
  fullName: string;
}