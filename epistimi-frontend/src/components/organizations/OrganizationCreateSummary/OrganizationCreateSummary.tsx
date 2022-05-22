import './OrganizationCreateSummary.scss';
import { IconCheck, IconLock, IconUser } from '@tabler/icons';
import { OrganizationRegisterResponse } from '../../../dto/organization';
import { TextInput, Title } from '@mantine/core';

interface OrganizationCreateSummaryProps {
  newOrganization: OrganizationRegisterResponse;
}

export const OrganizationCreateSummary = (
  { newOrganization }: OrganizationCreateSummaryProps,
): JSX.Element => {
  return (
    <div className={'organization-create-summary'}>
      <div className={'organization-create-summary-head'}>
        <IconCheck size={60}/>
        <Title order={2}>
          Zarejestrowano nową placówkę
        </Title>
        {newOrganization.name}
      </div>

      <Title order={3}>
        Konto administratora placówki
      </Title>
      <div className={'organization-create-summary-credentials'}>
        <TextInput
          icon={<IconUser size={18}/>}
          label={'Nazwa użytkownika'}
          readOnly={true}
          value={newOrganization.admin.user.username}/>
        <TextInput
          icon={<IconLock size={18}/>}
          label={'Hasło'}
          readOnly={true}
          value={newOrganization.admin.password}/>
      </div>
    </div>
  );
};
