import './OrganizationColorStatus.scss';
import { OrganizationStatus } from '../../../dto/organization';
import { Text } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface OrganizationColorStatusProps {
  status: OrganizationStatus;
}

export const OrganizationColorStatus = ({ status }: OrganizationColorStatusProps): JSX.Element => {
  const { t } = useTranslation();
  switch (status) {
    case OrganizationStatus.ENABLED:
      return (
        <Text color={'green'} className={'organization-status'}>
          {t('organizations.organizationColorStatus.enabled').toUpperCase()}
        </Text>
      );
    case OrganizationStatus.DISABLED:
      return (
        <Text color={'red'} className={'organization-status'}>
          {t('organizations.organizationColorStatus.disabled').toUpperCase()}
        </Text>
      );
  }
};
