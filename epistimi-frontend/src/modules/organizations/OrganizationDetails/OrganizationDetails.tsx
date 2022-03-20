import './OrganizationDetails.scss';
import { Back, Button, MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { Block, Done, Edit } from '@mui/icons-material';
import { Modal } from '../../../components/Modal';
import { OrganizationColorStatus } from '../OrganizationColorStatus';
import { OrganizationDetailsKeyValue } from '../OrganizationDetailsKeyValue';
import { OrganizationDetailsStatsTile } from '../OrganizationDetailsStatsTile';
import { OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
import { OrganizationStatusChange } from '../OrganizationStatusChange';
import { useEffect, useState } from 'react';
import { useFetch } from '../../../hooks/useFetch';
import { useParams } from 'react-router-dom';

export const OrganizationDetails = (): JSX.Element => {
  const { id } = useParams();
  const { data: organization, loading, error } = useFetch<OrganizationResponse>(`/api/organization/${id}`);

  const [statusChangeModalOpened, setStatusChangeModalOpened] = useState<boolean>(false);
  const [editModalOpened, setEditModalOpened] = useState<boolean>(false);

  useEffect(() => {
    document.title = 'Szczegóły placówki – Epistimi';
  }, []);

  if (loading) {
    return <Spinner/>;
  }

  const handleStatusChange = (updatedOrganization: OrganizationResponse): void => {
    organization && (organization.status = updatedOrganization.status);
    setStatusChangeModalOpened(false);
  };

  return (
    <div className={'organization-details'}>
      {loading &&
        <Spinner/>}
      {error &&
        <MessageBox style={MessageBoxStyle.WARNING}>
          Nie udało się załadować szczegółów organizacji
        </MessageBox>}
      {organization && <>
        <Modal open={statusChangeModalOpened} onClose={() => setStatusChangeModalOpened(false)}>
          <OrganizationStatusChange organization={organization} onStatusChange={handleStatusChange}/>
        </Modal>
        <Modal open={editModalOpened} onClose={() => setEditModalOpened(false)}>
          edit modal
        </Modal>
        <div className={'organization-header'}>
          <div className={'organization-header-group'}>
            <Back/>
            <h2>Szczegóły placówki</h2>
          </div>
          <div className={'organization-header-group'}>
            {organization.status === OrganizationStatus.ENABLED &&
              <Button icon={<Block/>} onClick={() => setStatusChangeModalOpened(true)}>Dezaktywuj placówkę</Button>}
            {organization.status === OrganizationStatus.DISABLED &&
              <Button icon={<Done/>} onClick={() => setStatusChangeModalOpened(true)}>Aktywuj placówkę</Button>}
            <Button icon={<Edit/>} onClick={() => setEditModalOpened(true)}>Edytuj dane</Button>
          </div>
        </div>
        <div className={'organization-data'}>
          <div className={'organization-name'}>
            {organization.name}
          </div>
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
      </>}
    </div>
  );
};
