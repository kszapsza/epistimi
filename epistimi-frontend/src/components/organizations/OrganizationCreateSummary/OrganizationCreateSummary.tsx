import './OrganizationCreateSummary.scss';
import { IconCheck, IconLock, IconUser } from '@tabler/icons';
import { OrganizationRegisterResponse } from '../../../dto/organization';
import { TextInput, Title } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface OrganizationCreateSummaryProps {
  newOrganization: OrganizationRegisterResponse;
}

export const OrganizationCreateSummary = (
  { newOrganization }: OrganizationCreateSummaryProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <div className={'organization-create-summary'}>
      <div className={'organization-create-summary-head'}>
        <IconCheck size={60}/>
        <Title order={2}>
          {t('organizations.organizationCreateSummary.organizationRegistered')}
        </Title>
        {newOrganization.name}
      </div>

      <Title order={3}>
        {t('organizations.organizationCreateSummary.organizationAdminAccount')}
      </Title>
      <div className={'organization-create-summary-credentials'}>
        <TextInput
          icon={<IconUser size={16}/>}
          label={t('organizations.organizationCreateSummary.username')}
          readOnly={true}
          value={newOrganization.admin.user.username}/>
        <TextInput
          icon={<IconLock size={16}/>}
          label={t('organizations.organizationCreateSummary.password')}
          readOnly={true}
          value={newOrganization.admin.password}/>
      </div>
    </div>
  );
};
