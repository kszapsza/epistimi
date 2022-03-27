import './OrganizationsListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { Create, Done, ErrorOutline } from '@mui/icons-material';
import { OrganizationEdit, OrganizationEditVariant } from '../OrganizationEdit';
import { OrganizationResponse, OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { OrganizationsListingTile } from '../OrganizationsListingTile';
import { useDisclosure } from '@mantine/hooks';
import { useEffect } from 'react';
import { useFetch } from '../../../hooks/useFetch';

export const OrganizationsListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<OrganizationsResponse>('api/organization');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);

  useEffect(() => {
    document.title = 'Placówki – Epistimi';
  }, []);

  const activeCount: number = data?.organizations.filter((organization) => {
    return organization.status == OrganizationStatus.ENABLED;
  }).length ?? 0;

  const onOrganizationCreate = (organization: OrganizationResponse) => {
    data?.organizations.push(organization);
    createModalHandlers.close();
    createdMessageHandlers.open();
  };

  return (
    <div className={'organizations'}>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={'Tworzenie nowej placówki'}
      >
        <OrganizationEdit
          submitCallback={onOrganizationCreate}
          variant={OrganizationEditVariant.CREATE}
        />
      </Modal>

      <div className={'organizations-actions'}>
        <Title order={2}>Placówki</Title>
        <Button
          leftIcon={<Create style={{ fontSize: '16px' }}/>}
          onClick={createModalHandlers.open}
          variant={'default'}
        >
          Utwórz nową
        </Button>
      </div>

      {error &&
        <Alert icon={<ErrorOutline style={{ fontSize: '16px' }}/>} color="red">
          Nie udało się załadować listy placówek!
        </Alert>}
      {createdMessageOpened &&
        <Alert icon={<Done style={{ fontSize: '16px' }}/>} color="green">
          Pomyślnie utworzono nową placówkę
        </Alert>}

      {loading && <Loader/>}

      {data &&
        <div className={'organizations-listing'}>
          {data.organizations.map(({ id, name, admin, status }) =>
            <OrganizationsListingTile
              key={id}
              id={id}
              name={name}
              admin={admin.username}
              status={status}
            />)}
          <div className={'organizations-listing-summary'}>
            Łącznie: {data.organizations.length}, w tym aktywnych: {activeCount}.
          </div>
        </div>}
    </div>
  );
};