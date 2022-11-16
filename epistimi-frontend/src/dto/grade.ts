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

export const enum GradeValue {
  UNSATISFACTORY = 'UNSATISFACTORY',
  ACCEPTABLE_MINUS = 'ACCEPTABLE_MINUS',
  ACCEPTABLE = 'ACCEPTABLE',
  ACCEPTABLE_PLUS = 'ACCEPTABLE_PLUS',
  SATISFACTORY_MINUS = 'SATISFACTORY_MINUS',
  SATISFACTORY = 'SATISFACTORY',
  SATISFACTORY_PLUS = 'SATISFACTORY_PLUS',
  GOOD_MINUS = 'GOOD_MINUS',
  GOOD = 'GOOD',
  GOOD_PLUS = 'GOOD_PLUS',
  VERY_GOOD_MINUS = 'VERY_GOOD_MINUS',
  VERY_GOOD = 'VERY_GOOD',
  VERY_GOOD_PLUS = 'VERY_GOOD_PLUS',
  EXCELLENT = 'EXCELLENT',
  NO_ASSIGNMENT = 'NO_ASSIGNMENT',
  UNPREPARED = 'UNPREPARED',
  UNCLASSIFIED = 'UNCLASSIFIED',
  ATTENDED = 'ATTENDED',
  DID_NOT_ATTEND = 'ATTEND',
  PASSED = 'PASSED',
  FAILED = 'FAILED',
  ABSENT = 'ABSENT',
  EXEMPT = 'EXEMPT',
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