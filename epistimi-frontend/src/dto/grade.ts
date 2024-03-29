import { GradeValue } from './grade-values';

export interface GradeResponse {
  id: string;
  issuedBy: GradeTeacherResponse;
  category: GradeCategorySimpleResponse;
  semester: number;
  issuedAt: Date;
  updatedAt?: Date;
  value: GradeValueResponse;
  weight: number;
  countTowardsAverage: boolean;
  comment?: string;
}

export interface GradeTeacherResponse {
  id: string;
  academicTitle?: string;
  firstName: string;
  lastName: string;
}

export interface GradeCategorySimpleResponse {
  id: string;
  name: string;
  color?: string;
}

export interface GradeValueResponse {
  displayName: string;
  fullName: string;
}

export interface GradeIssueRequest {
  subjectId: string;
  studentId: string;
  categoryId: string;
  value: GradeValue;
  semester: number;
  weight: number;
  countTowardsAverage: boolean;
  comment?: string;
}