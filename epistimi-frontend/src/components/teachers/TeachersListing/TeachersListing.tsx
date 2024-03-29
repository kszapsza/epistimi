import './TeachersListing.scss';
import { Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconInfoCircle, IconPlus } from '@tabler/icons';
import { TeacherCreate, TeachersListingTile } from '../../teachers';
import { TeachersResponse } from '../../../dto/teacher';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const TeachersListing = (): JSX.Element => {
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  const { data, loading, error, reload } = useFetch<TeachersResponse>('/api/teacher');
  const { t } = useTranslation();

  useDocumentTitle(t('teachers.teachersListing.teachers'));

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={t('teachers.teachersListing.addingNewTeacher')}
      >
        <TeacherCreate onTeacherRegistered={reload}/>
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
          <Alert icon={<IconAlertCircle size={16}/>} title={t('common.error')} color={'red'}>
            {t('teachers.teachersListing.couldNotLoad')}
          </Alert>}

        {data?.teachers.length === 0 &&
          <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
            {t('teachers.teachersListing.noTeachers')}
          </Alert>}

        {data &&
          <div className={'teachers-entries'}>
            {data.teachers.map((teacher) => (
              <TeachersListingTile key={teacher.id} teacher={teacher}/>
            ))}
          </div>
        }
      </div>
    </>
  );
};
