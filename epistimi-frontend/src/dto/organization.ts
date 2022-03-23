import { Address } from './address';
import { UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  status: OrganizationStatus;
  director: UserResponse;
  address: Address;
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
