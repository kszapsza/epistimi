import './OrganizationsListing.scss';
import { Button, MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { Create, Done, ErrorOutline } from '@mui/icons-material';
import { Modal } from '../../../components/Modal';
import { OrganizationEdit } from '../OrganizationEdit';
import { OrganizationResponse, OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { OrganizationsListingTile } from '../OrganizationsListingTile';
import { useEffect, useState } from 'react';
import { useFetch } from '../../../hooks/useFetch';

export const OrganizationsListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<OrganizationsResponse>('api/organization');
  const [createModalOpen, setCreateModalOpen] = useState<boolean>(false);
  const [createdMessageOpen, setCreatedMessageOpen] = useState<boolean>(false);

  useEffect(() => {
    document.title = 'Placówki – Epistimi';
  }, []);

  const activeCount: number = data?.organizations.filter((organization) => {
    return organization.status == OrganizationStatus.ENABLED;
  }).length ?? 0;

  const onOrganizationCreate = (organization: OrganizationResponse) => {
    data?.organizations.push(organization);
    setCreateModalOpen(false);
    setCreatedMessageOpen(true);
  };

  return (
    <div className={'organizations'}>
      <Modal open={createModalOpen} onClose={() => setCreateModalOpen(false)}>
        <OrganizationEdit submitCallback={onOrganizationCreate} variant={'create'}/>
      </Modal>
      <div className={'organizations-actions'}>
        <h2>Placówki</h2>
        <Button
          icon={<Create/>}
          onClick={() => setCreateModalOpen(true)}
        >
          Utwórz nową
        </Button>
      </div>

      {error &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutline/>}>
          Nie udało się załadować listy placówek!
        </MessageBox>}
      {createdMessageOpen &&
        <MessageBox style={MessageBoxStyle.CONSTRUCTIVE} icon={<Done/>}>
          Pomyślnie utworzono nową placówkę
        </MessageBox>}

      {loading && <Spinner/>}

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
