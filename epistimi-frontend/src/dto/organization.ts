import { Address } from './address';
import { Location } from './location';
import { UserRegisterRequest, UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  address: Address;
  location?: Location;
}

export interface OrganizationsResponse {
  organizations: OrganizationResponse[];
}

export interface OrganizationRegisterRequest {
  name: string;
  admin: UserRegisterRequest;
  address: Address;
}

export interface OrganizationRegisterResponse {
  id: string;
  name: string;
  admin: NewUserResponse;
  address: Address;
  location?: Location;
}

interface NewUserResponse {
  user: UserResponse;
  password: string;
}

export interface OrganizationUpdateRequest {
  name: string;
  address: Address;
}
