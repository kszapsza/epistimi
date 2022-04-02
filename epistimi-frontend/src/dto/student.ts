import { ParentResponse } from './parent';
import { UserResponse } from './user';

export interface StudentResponse {
  id: string;
  user: UserResponse;
  parents: ParentResponse[];
}
