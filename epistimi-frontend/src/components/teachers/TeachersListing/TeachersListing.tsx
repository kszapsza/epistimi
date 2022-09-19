import './TeachersListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconPlus } from '@tabler/icons';
import { TeacherCreate, TeachersListingTile } from '../../teachers';
import { TeacherRegisterResponse, TeachersResponse } from '../../../dto/teacher';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const TeachersListing = (): JSX.Element => {
  const { data, loading, error, setData } = useFetch<TeachersResponse>('/api/teacher');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  const { t } = useTranslation();
  useDocumentTitle(t('teachers.teachersListing.teachers'));

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
        title={t('teachers.teachersListing.addingNewTeacher')}
      >
        <TeacherCreate onTeacherRegistered={appendCreatedTeacher}/>
      </Modal>
      <div className={'teachers'}>
        <div className={'teachers-actions'}>
          <Title order={2}>
            {t('teachers.teachersListing.teachers')}
          </Title>
          <Button
            leftIcon={<IconPlus size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            {t('teachers.teachersListing.addTeacher')}
          </Button>
        </div>

        {loading && <Loader style={{ width: '100%' }}/>}

        {error &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            {t('teachers.teachersListing.couldNotLoad')}
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
