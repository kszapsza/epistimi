import { UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  status: OrganizationStatus;
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
}

export interface OrganizationChangeStatusRequest {
  status: OrganizationStatus;
}
