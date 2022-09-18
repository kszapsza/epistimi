import './OrganizationsListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconInfoCircle, IconPencil } from '@tabler/icons';
import { OrganizationCreate, OrganizationsListingTile } from '../../organizations';
import { OrganizationRegisterResponse, OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';

export const OrganizationsListing = (): JSX.Element => {
  const { data, setData, loading, error } = useFetch<OrganizationsResponse>('api/organization');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  useDocumentTitle('Placówki');

  const activeCount: number = data?.organizations
    .filter((organization) => organization.status === OrganizationStatus.ENABLED)
    .length ?? 0;

  const onOrganizationCreate = (response: OrganizationRegisterResponse) => {
    data && setData({
      organizations: [...data.organizations, {
        id: response.id,
        name: response.name,
        admin: response.admin.user,
        status: response.status,
        address: response.address,
        location: response.location,
      }],
    });
  };

  return (
    <div className={'organizations'}>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'xl'}
        title={'Tworzenie nowej placówki'}
      >
        <OrganizationCreate
          submitCallback={onOrganizationCreate}
        />
      </Modal>

      <div className={'organizations-actions'}>
        <Title order={2}>Placówki</Title>
        <Button
          leftIcon={<IconPencil size={16}/>}
          onClick={createModalHandlers.open}
          variant={'default'}
        >
          Utwórz nową
        </Button>
      </div>

      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
          Nie udało się załadować listy placówek!
        </Alert>}

      {data?.organizations?.length === 0 &&
        <Alert icon={<IconInfoCircle size={16}/>} color="blue">
          W systemie nie zarejestrowano jeszcze żadnych placówek!
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
