import './OrganizationDetails.scss';
import { ActionIcon, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconArrowBack, IconBan, IconCheck, IconPencil } from '@tabler/icons';
import { Link, useParams } from 'react-router-dom';
import {
  OrganizationColorStatus,
  OrganizationDetailsKeyValue,
  OrganizationDetailsLocation,
  OrganizationDetailsStatsTile,
  OrganizationStatusChange,
  OrganizationUpdate,
} from '../../organizations';
import { OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
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

  const [statusChangeModalOpened, statusChangeModalHandlers] = useDisclosure(false);
  const [editModalOpened, editModalHandlers] = useDisclosure(false);
  const [updatedMessageOpened, updatedMessageHandlers] = useDisclosure(false);

  useDocumentTitle(organization && organization.name);

  if (loading) {
    return <Loader/>;
  }

  const onOrganizationStatusChange = (updatedOrganization: OrganizationResponse): void => {
    organization && (organization.status = updatedOrganization.status);
    statusChangeModalHandlers.close();
  };

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
          onClose={statusChangeModalHandlers.close}
          opened={statusChangeModalOpened}
          size={'md'}
          title={t('organizations.organizationDetails.changingOrganizationStatus')}
        >
          <OrganizationStatusChange
            organization={organization}
            onStatusChange={onOrganizationStatusChange}
          />
        </Modal>
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
            {organization.status === OrganizationStatus.ENABLED &&
              <Button
                leftIcon={<IconBan size={16}/>}
                onClick={statusChangeModalHandlers.open}
                variant={'default'}>
                {t('organizations.organizationDetails.disableOrganization')}
              </Button>}
            {organization.status === OrganizationStatus.DISABLED &&
              <Button
                leftIcon={<IconCheck size={16}/>}
                onClick={statusChangeModalHandlers.open}
                variant={'default'}>
                {t('organizations.organizationDetails.enableOrganization')}
              </Button>}
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
            <OrganizationDetailsKeyValue
              label={t('organizations.organizationDetails.status')}
              value={<OrganizationColorStatus status={organization.status}/>}
            />
          </div>
        </div>
        <div className={'organization-stats'}>
          <OrganizationDetailsStatsTile label={t('organizations.organizationDetails.activeCourses')} value={'N/A'}/>
          <OrganizationDetailsStatsTile label={t('organizations.organizationDetails.activeStudents')} value={'N/A'}/>
          <OrganizationDetailsStatsTile label={t('organizations.organizationDetails.activeTeachers')} value={'N/A'}/>
        </div>
        <OrganizationDetailsLocation
          address={organization.address}
          location={organization.location}
        />
      </>}
    </div>
  );
};
