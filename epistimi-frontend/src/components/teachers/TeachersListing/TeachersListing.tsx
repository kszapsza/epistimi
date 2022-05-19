import './TeachersListing.scss';
import { Button, Loader, Modal, Title } from '@mantine/core';
import { IconPlus } from '@tabler/icons';
import { TeachersListingTile } from '../TeachersListingTile';
import { TeachersResponse } from '../../../dto/teacher';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks/useFetch';

export const TeachersListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<TeachersResponse>('/api/teacher');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={'Dodaj nowego nauczyciela'}
      >
        nothing here just yet!!! to be done.
      </Modal>
      <div className={'teachers'}>
        <div className={'teachers-actions'}>
          <Title order={2}>Nauczyciele</Title>
          <Button
            leftIcon={<IconPlus size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            Dodaj nauczyciela
          </Button>
        </div>

        {data &&
          <div className={'teachers-entries'}>
            {data.teachers.map((teacher) => (<>
                <TeachersListingTile teacher={teacher}/>
                <TeachersListingTile teacher={teacher}/>
                <TeachersListingTile teacher={teacher}/>
              </>
            ))}
          </div>
        }

        {loading && <Loader/>}
      </div>
    </>
  );
};
