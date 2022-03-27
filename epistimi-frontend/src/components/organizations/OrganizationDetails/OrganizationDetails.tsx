import './OrganizationDetails.scss';
import { ActionIcon, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { AlertCircle, ArrowBack, Ban, Check, Pencil } from 'tabler-icons-react';
import { Link, useParams } from 'react-router-dom';
import { OrganizationColorStatus } from '../OrganizationColorStatus';
import { OrganizationDetailsKeyValue } from '../OrganizationDetailsKeyValue';
import { OrganizationDetailsLocation } from '../OrganizationDetailsLocation';
import { OrganizationDetailsStatsTile } from '../OrganizationDetailsStatsTile';
import { OrganizationEdit, OrganizationEditVariant } from '../OrganizationEdit';
import { OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
import { OrganizationStatusChange } from '../OrganizationStatusChange';
import { useDisclosure } from '@mantine/hooks';
import { useEffect } from 'react';
import { useFetch } from '../../../hooks/useFetch';

export const OrganizationDetails = (): JSX.Element => {
  const { id } = useParams();

  const {
    data: organization,
    setData: setOrganization,
    loading,
    error,
  } = useFetch<OrganizationResponse>(`/api/organization/${id}`);

  const [statusChangeModalOpened, statusChangeModalHandlers] = useDisclosure(false);
  const [editModalOpened, editModalHandlers] = useDisclosure(false);
  const [updatedMessageOpened, updatedMessageHandlers] = useDisclosure(false);

  useEffect(() => {
    document.title = 'Szczegóły placówki – Epistimi';
  }, []);

  if (loading) {
    return <Loader/>;
  }

  const onOrganizationStatusChange = (updatedOrganization: OrganizationResponse): void => {
    organization && (organization.status = updatedOrganization.status);
    statusChangeModalHandlers.close();
  };

  const onOrganizationUpdate = (updatedOrganization: OrganizationResponse): void => {
    setOrganization(updatedOrganization);
    editModalHandlers.close();
    updatedMessageHandlers.open();
  };

  return (
    <div className={'organization-details'}>
      {loading &&
        <Loader/>}
      {(error || updatedMessageOpened) &&
        <div className={'organization-mbox-dock'}>
          {error &&
            <Alert icon={<AlertCircle size={16}/>} color="red">
              Nie udało się załadować szczegółów organizacji
            </Alert>}
          {updatedMessageOpened &&
            <Alert icon={<Check size={16}/>} color="green">
              Zaktualizowano dane placówki
            </Alert>}
        </div>}
      {organization && <>
        <Modal
          onClose={statusChangeModalHandlers.close}
          opened={statusChangeModalOpened}
          size={'md'}
          title={'Zmiana statusu placówki'}
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
          title={'Edytowanie placówki'}
        >
          <OrganizationEdit
            submitCallback={onOrganizationUpdate}
            variant={OrganizationEditVariant.UPDATE}
            organizationId={organization.id}
            defaults={{ ...organization }}
          />
        </Modal>
        <div className={'organization-header'}>
          <div className={'organization-header-group'}>
            <ActionIcon variant={'transparent'} component={Link} to={'./..'}>
              <ArrowBack size={18}/>
            </ActionIcon>
          </div>
          <div className={'organization-header-group'}>
            {organization.status === OrganizationStatus.ENABLED &&
              <Button
                leftIcon={<Ban size={16}/>}
                onClick={statusChangeModalHandlers.open}
                variant={'default'}>
                Dezaktywuj placówkę
              </Button>}
            {organization.status === OrganizationStatus.DISABLED &&
              <Button
                leftIcon={<Check size={16}/>}
                onClick={statusChangeModalHandlers.open}
                variant={'default'}>
                Aktywuj placówkę
              </Button>}
            <Button
              leftIcon={<Pencil size={16}/>}
              onClick={editModalHandlers.open}
              variant={'default'}>
              Edytuj dane
            </Button>
          </div>
        </div>
        <div className={'organization-data'}>
          <Title order={4} className={'organization-name'}>
            {organization.name}
          </Title>
          <div className={'organization-entries'}>
            <OrganizationDetailsKeyValue
              label={'Id:'}
              value={<samp>{organization.id}</samp>}
            />
            <OrganizationDetailsKeyValue
              label={'Administrator:'}
              value={`${organization.admin.lastName} ${organization.admin.firstName} (${organization.admin.username})`}
            />
            <OrganizationDetailsKeyValue
              label={'Dyrektor:'}
              value={`${organization.director.lastName} ${organization.director.firstName} (${organization.director.username})`}
            />
            <OrganizationDetailsKeyValue
              label={'Status:'}
              value={<OrganizationColorStatus status={organization.status}/>}
            />
          </div>
        </div>
        <div className={'organization-stats'}>
          <OrganizationDetailsStatsTile label={'Aktywnych klas:'} value={'N/A'}/>
          <OrganizationDetailsStatsTile label={'Aktywnych uczniów:'} value={'N/A'}/>
          <OrganizationDetailsStatsTile label={'Aktywnych nauczycieli:'} value={'N/A'}/>
        </div>
        <OrganizationDetailsLocation
          address={organization.address}
          location={organization.location}
        />
      </>}
    </div>
  );
};
