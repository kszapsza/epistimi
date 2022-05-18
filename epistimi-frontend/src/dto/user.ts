import { Address } from './address';

export interface UserResponse {
  id: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  username: string;
  pesel?: string,
  sex?: UserSex,
  email?: string,
  phoneNumber?: string,
  address?: Address,
}

export enum UserRole {
  EPISTIMI_ADMIN = 'EPISTIMI_ADMIN',
  ORGANIZATION_ADMIN = 'ORGANIZATION_ADMIN',
  TEACHER = 'TEACHER',
  STUDENT = 'STUDENT',
  PARENT = 'PARENT',
}

export enum UserSex {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER',
}

export interface UsersResponse {
  users: UserResponse[];
}

export interface UserRegisterRequest {
  firstName: string;
  lastName: string;
  role: UserRole;
  username?: string;
  password?: string;
  pesel?: string;
  sex?: UserSex;
  email?: string;
  phoneNumber?: string;
  address?: Address;
}
