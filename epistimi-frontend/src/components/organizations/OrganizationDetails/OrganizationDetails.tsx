import './OrganizationDetails.scss';
import { ActionIcon, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconArrowBack, IconCheck, IconPencil } from '@tabler/icons';
import { Link, useParams } from 'react-router-dom';
import {
  OrganizationDetailsKeyValue,
  OrganizationDetailsLocation,
  OrganizationUpdate,
} from '../../organizations';
import { OrganizationResponse } from '../../../dto/organization';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const OrganizationDetails = (): JSX.Element => {
  const { id } = useParams();
  const { t } = useTranslation();

  const {
    data: organization,
    loading,
    error,
    reload,
  } = useFetch<OrganizationResponse>(`/api/organization/${id}`);

  const [editModalOpened, editModalHandlers] = useDisclosure(false);
  const [updatedMessageOpened, updatedMessageHandlers] = useDisclosure(false);

  useDocumentTitle(organization && organization.name);

  if (loading) {
    return <Loader/>;
  }

  const onOrganizationUpdate = (): void => {
    reload();
    editModalHandlers.close();
    updatedMessageHandlers.open();
  };

  return (
    <div className={'organization-details'}>
      {loading && <Loader/>}
      {(error || updatedMessageOpened) &&
        <div className={'organization-mbox-dock'}>
          {error &&
            <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
              {t('organizations.organizationDetails.couldNotLoadDetails')}
            </Alert>}
          {updatedMessageOpened &&
            <Alert icon={<IconCheck size={16}/>} color={'green'}>
              {t('organizations.organizationDetails.dataUpdated')}
            </Alert>}
        </div>}
      {organization && <>
        <Modal
          onClose={editModalHandlers.close}
          opened={editModalOpened}
          size={'lg'}
          title={t('organizations.organizationDetails.editingOrganization')}
        >
          <OrganizationUpdate
            submitCallback={onOrganizationUpdate}
            updatedOrganization={{ ...organization }}
          />
        </Modal>
        <div className={'organization-header'}>
          <div className={'organization-header-group'}>
            <ActionIcon variant={'transparent'} component={Link} to={'./..'}>
              <IconArrowBack size={18}/>
            </ActionIcon>
          </div>
          <div className={'organization-header-group'}>
            <Button
              leftIcon={<IconPencil size={16}/>}
              onClick={editModalHandlers.open}
              variant={'default'}>
              {t('organizations.organizationDetails.editData')}
            </Button>
          </div>
        </div>
        <div className={'organization-data'}>
          <Title order={4} className={'organization-name'}>
            {organization.name}
          </Title>
          <div className={'organization-entries'}>
            <OrganizationDetailsKeyValue
              label={t('organizations.organizationDetails.id')}
              value={<samp>{organization.id}</samp>}
            />
            <OrganizationDetailsKeyValue
              label={t('organizations.organizationDetails.admin')}
              value={`${organization.admin.lastName} ${organization.admin.firstName} (${organization.admin.username})`}
            />
          </div>
        </div>
        <OrganizationDetailsLocation
          address={organization.address}
          location={organization.location}
        />
      </>}
    </div>
  );
};
