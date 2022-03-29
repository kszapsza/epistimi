import { Address } from './address';
import { Location } from './location';
import { UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  status: OrganizationStatus;
  director: UserResponse;
  address: Address;
  location?: Location;
}

export interface OrganizationsResponse {
  organizations: OrganizationResponse[];
}

export enum OrganizationStatus {
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED',
}

export interface OrganizationRegisterRequest {
  name: string;
  adminId: string;
  directorId: string;
  address: Address;
}

export interface OrganizationChangeStatusRequest {
  status: OrganizationStatus;
}
