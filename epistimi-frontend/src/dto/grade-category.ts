export interface GradeCategoryResponse {
  id: string;
  subject: GradeCategorySubjectResponse;
  name: string;
  defaultWeight: number;
  color?: string;
}

export interface GradeCategorySubjectResponse {
  id: string;
  name: string;
}

export interface GradeCategoriesResponse {
  subject: GradeCategorySubjectResponse;
  categories: GradeCategoriesResponseEntry[];
}

export interface GradeCategoriesResponseEntry {
  id: string;
  name: string;
  defaultWeight: number;
  color?: string;
}

export interface GradeCategoryCreateRequest {
  subjectId: string;
  name: string;
  defaultWeight: number;
  color?: string;
}