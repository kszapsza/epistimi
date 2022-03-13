import './Organizations.scss';
import { Button, MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { Create, Done, ErrorOutline } from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { Modal } from '../../../components/Modal';
import { OrganizationColorStatus } from '../OrganizationColorStatus/OrganizationColorStatus';
import { OrganizationCreate } from '../OrganizationCreate';
import { OrganizationResponse, OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { useEffect, useState } from 'react';
import { useFetch } from '../../../hooks/useFetch';

export const Organizations = (): JSX.Element => {
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
        <OrganizationCreate onCreated={onOrganizationCreate}/>
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
        <>
          <table className={'organizations-listing'}>
            <tbody>
            <tr>
              <th>Id</th>
              <th>Nazwa</th>
              <th>Administrator</th>
              <th>Status</th>
            </tr>
            {data.organizations.map(({ id, name, admin, status }) =>
              <tr key={id}>
                <td><Link to={`./${id}`}><samp>{id}</samp></Link></td>
                <td>{name}</td>
                <td>{admin.username}</td>
                <td>{<OrganizationColorStatus status={status}/>}</td>
              </tr>,
            )}
            </tbody>
          </table>
          <p>Łącznie: {data.organizations.length}, w tym aktywnych: {activeCount}.</p>
        </>}
    </div>
  );
};
