import { Address } from './address';
import { Location } from './location';
import { UserRegisterRequest, UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  status: OrganizationStatus;
  address: Address;
  location?: Location;
}

export interface OrganizationsResponse {
  organizations: OrganizationResponse[];
}

export const enum OrganizationStatus {
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED',
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
  status: OrganizationStatus;
  address: Address;
  location?: Location;
}

interface NewUserResponse {
  user: UserResponse;
  password: string;
}

export interface OrganizationChangeStatusRequest {
  status: OrganizationStatus;
}

export interface OrganizationUpdateRequest {
  name: string;
  address: Address;
}
