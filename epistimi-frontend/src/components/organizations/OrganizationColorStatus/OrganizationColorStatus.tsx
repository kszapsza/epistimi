import './OrganizationColorStatus.scss';
import { OrganizationStatus } from '../../../dto/organization';
import { Text } from '@mantine/core';

interface OrganizationColorStatusProps {
  status: OrganizationStatus;
}

export const OrganizationColorStatus = ({ status }: OrganizationColorStatusProps): JSX.Element => {
  switch (status) {
    case OrganizationStatus.ENABLED:
      return <Text color={'green'} className={'organization-status'}>AKTYWNA</Text>;
    case OrganizationStatus.DISABLED:
      return <Text color={'red'} className={'organization-status'}>NIEAKTYWNA</Text>;
  }
};
