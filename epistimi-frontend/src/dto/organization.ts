import { UserResponse } from './user';

export interface OrganizationResponse {
  id: string;
  name: string;
  admin: UserResponse;
  status: string;
}

export interface OrganizationsResponse {
  organizations: OrganizationResponse[];
}

export enum OrganizationStatus {
  ENABLED = 'ENABLED',
  DISABLED = 'DISABLED',
}
