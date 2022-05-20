import './TeachersListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconPlus } from '@tabler/icons';
import { TeacherCreate } from '../TeacherCreate';
import { TeacherRegisterResponse, TeachersResponse } from '../../../dto/teacher';
import { TeachersListingTile } from '../TeachersListingTile';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks/useFetch';

export const TeachersListing = (): JSX.Element => {
  const { data, loading, error, setData } = useFetch<TeachersResponse>('/api/teacher');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  const appendCreatedTeacher = (newTeacher: TeacherRegisterResponse): void => {
    data && setData({
      teachers: [
        ...data.teachers,
        {
          id: newTeacher.id,
          user: newTeacher.newUser.user,
          academicTitle: newTeacher.academicTitle,
        },
      ],
    });
  };

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={'Dodaj nowego nauczyciela'}
      >
        <TeacherCreate onTeacherRegistered={appendCreatedTeacher}/>
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

        {loading && <Loader/>}

        {error &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            Nie udało się załadować listy nauczycieli
          </Alert>}

        {data &&
          <div className={'teachers-entries'}>
            {data.teachers.map((teacher) => (
              <TeachersListingTile teacher={teacher}/>
            ))}
          </div>
        }
      </div>
    </>
  );
};
