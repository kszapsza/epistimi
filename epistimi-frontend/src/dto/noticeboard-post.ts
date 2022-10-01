import { UserResponse } from './user';

export interface NoticeboardPostResponse {
  id: string;
  author: UserResponse;
  createdDate: Date;
  updatedDate: Date;
  title: string;
  content: string;
}

export interface NoticeboardPostResponse {
  posts: NoticeboardPostResponse[];
}