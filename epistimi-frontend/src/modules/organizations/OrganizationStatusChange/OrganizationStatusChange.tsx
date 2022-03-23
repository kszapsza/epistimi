import './OrganizationStatusChange.scss';
import { Block, Done } from '@mui/icons-material';
import { Button, ButtonStyle } from '../../../components';
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
          <h3>Dezaktywacja placówki</h3>
          <p>
            Czy na pewno chcesz zdezaktywować placówkę <strong>{organization.name}</strong>?
          </p>
          <Button
            icon={<Block/>}
            onClick={handleDisable}
            style={ButtonStyle.DESTRUCTIVE}
          >Dezaktywuj</Button>
        </div>
      );
    } else {
      return (
        <div className={'organization-status-change'}>
          <h3>Aktywacja placówki</h3>
          <p>
            Czy na pewno chcesz ponownie aktywować placówkę <strong>{organization.name}</strong>?
          </p>
          <Button
            icon={<Done/>}
            onClick={handleEnable}
            style={ButtonStyle.CONSTRUCTIVE}
          >Aktywuj</Button>
        </div>
      );
    }
  };
