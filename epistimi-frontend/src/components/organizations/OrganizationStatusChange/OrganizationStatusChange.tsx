import './OrganizationStatusChange.scss';
import { Ban, Check } from 'tabler-icons-react';
import { Button } from '@mantine/core';
import { OrganizationChangeStatusRequest, OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
import axios, { AxiosResponse } from 'axios';

interface OrganizationStatusChangeProps {
  organization: OrganizationResponse;
  onStatusChange: (organization: OrganizationResponse) => void;
}

export const OrganizationStatusChange =
  ({ organization, onStatusChange }: OrganizationStatusChangeProps): JSX.Element => {
    const handleStatusChange = (status: OrganizationStatus): void => {
      axios.put<OrganizationResponse,
        AxiosResponse<OrganizationResponse>,
        OrganizationChangeStatusRequest>(
        `api/organization/${organization.id}/status`,
        { status },
      ).then((response) => {
          onStatusChange(response.data);
        },
      );
    };

    const handleDisable = (): void => handleStatusChange(OrganizationStatus.DISABLED);
    const handleEnable = (): void => handleStatusChange(OrganizationStatus.ENABLED);

    if (organization.status === OrganizationStatus.ENABLED) {
      return (
        <div className={'organization-status-change'}>
          <p>
            Czy na pewno chcesz zdezaktywować placówkę <strong>{organization.name}</strong>?
          </p>
          <Button
            leftIcon={<Ban/>}
            onClick={handleDisable}
            color={'red'}
          >Dezaktywuj</Button>
        </div>
      );
    } else {
      return (
        <div className={'organization-status-change'}>
          <p>
            Czy na pewno chcesz ponownie aktywować placówkę <strong>{organization.name}</strong>?
          </p>
          <Button
            leftIcon={<Check/>}
            onClick={handleEnable}
            color={'green'}
          >Aktywuj</Button>
        </div>
      );
    }
  };
