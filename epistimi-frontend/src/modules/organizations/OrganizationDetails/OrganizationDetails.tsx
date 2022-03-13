import './OrganizationDetails.scss';
import { Block, Edit } from '@mui/icons-material';
import { Button, Spinner } from '../../../components';
import { Modal } from '../../../components/Modal';
import { OrganizationResponse } from '../../../dto/organization';
import { useFetch } from '../../../hooks/useFetch';
import { useParams } from 'react-router-dom';
import { useState } from 'react';

export const OrganizationDetails = (): JSX.Element => {
  const { id } = useParams();
  const { data, loading, error } = useFetch<OrganizationResponse>(`/api/organization/${id}`);

  const [deactivateModalOpened, setDeactivateModalOpened] = useState<boolean>(false);
  const [editModalOpened, setEditModalOpened] = useState<boolean>(false);

  return (
    <div className={'organization-details'}>
      <Modal open={deactivateModalOpened} onClose={() => setDeactivateModalOpened(false)}>
        deactivate modal
      </Modal>
      <Modal open={editModalOpened} onClose={() => setEditModalOpened(false)}>
        edit modal
      </Modal>
      <div className={'organization-header'}>
        <h2>Szczegóły placówki</h2>
        <div className={'organization-actions'}>
          <Button icon={<Block/>} onClick={() => setDeactivateModalOpened(true)}>Dezaktywuj placówkę</Button>
          <Button icon={<Edit/>} onClick={() => setEditModalOpened(true)}>Edytuj dane</Button>
        </div>
      </div>
      <div className={'organization-data'}>
        {loading && <Spinner/>}
        {data && (
          <>
            <div className={'organization-name'}>{data.name}</div>
            <div className={'organization-values'}>
              123
            </div>
          </>
        )}
      </div>
    </div>
  );
};
