import './OrganizationsListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconInfoCircle, IconPencil } from '@tabler/icons';
import { OrganizationCreate, OrganizationsListingTile } from '../../organizations';
import { OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const OrganizationsListing = (): JSX.Element => {
  const { data, loading, error, reload } = useFetch<OrganizationsResponse>('api/organization');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  const { t } = useTranslation();
  useDocumentTitle(t('organizations.organizationsListing.organizations'));

  const activeCount: number = data?.organizations
    .filter((organization) => organization.status === OrganizationStatus.ENABLED)
    .length ?? 0;

  return (
    <div className={'organizations'}>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'xl'}
        title={t('organizations.organizationsListing.creatingNewOrganization')}
      >
        <OrganizationCreate
          submitCallback={reload}
        />
      </Modal>

      <div className={'organizations-actions'}>
        <Title order={2}>
          {t('organizations.organizationsListing.organizations')}
        </Title>
        <Button
          leftIcon={<IconPencil size={16}/>}
          onClick={createModalHandlers.open}
          variant={'default'}
        >
          {t('organizations.organizationsListing.createNew')}
        </Button>
      </div>

      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
          {t('organizations.organizationsListing.couldNotLoadOrganizationsList')}
        </Alert>}

      {data?.organizations?.length === 0 &&
        <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
          {t('organizations.organizationsListing.noOrganizationsRegistered')}
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
            {t('organizations.organizationsListing.summary', { total: data.organizations.length, active: activeCount })}
          </div>
        </div>}
    </div>
  );
};
