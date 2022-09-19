import './OrganizationStatusChange.scss';
import { Button } from '@mantine/core';
import { IconBan, IconCheck } from '@tabler/icons';
import { OrganizationChangeStatusRequest, OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

interface OrganizationStatusChangeProps {
  organization: OrganizationResponse;
  onStatusChange: (organization: OrganizationResponse) => void;
}

export const OrganizationStatusChange =
  ({ organization, onStatusChange }: OrganizationStatusChangeProps): JSX.Element => {

    const { t } = useTranslation();

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
            {t('organizations.organizationStatusChange.areYouSureYouWantToDisable', { name: organization.name })}
          </p>
          <Button
            leftIcon={<IconBan/>}
            onClick={handleDisable}
            color={'red'}
          >{t('organizations.organizationStatusChange.disable')}</Button>
        </div>
      );
    } else {
      return (
        <div className={'organization-status-change'}>
          <p>
            {t('organizations.organizationStatusChange.areYouSureYouWantToEnable', { name: organization.name })}
          </p>
          <Button
            leftIcon={<IconCheck/>}
            onClick={handleEnable}
            color={'green'}
          >{t('organizations.organizationStatusChange.enable')}</Button>
        </div>
      );
    }
  };
