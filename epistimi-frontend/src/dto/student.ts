import { ParentResponse } from './parent';
import { UserRegisterRequest, UserResponse } from './user';

export interface StudentResponse {
  id: string;
  user: UserResponse;
  parents: ParentResponse[];
}

export interface StudentRegisterRequest {
  courseId: string;
  user: UserRegisterRequest;
  parents: UserRegisterRequest[];
}

export interface StudentRegisterResponse {
  id: string;
  student: NewUserResponse;
  parents: NewParentResponse[];
}

interface NewUserResponse {
  user: UserResponse;
  password: string;
}

interface NewParentResponse {
  parent: ParentResponse;
  password: string;
}
