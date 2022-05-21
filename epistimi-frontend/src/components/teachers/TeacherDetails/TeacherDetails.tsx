import './TeacherDetails.scss';
import { Alert, Loader, Title } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { TeacherDetailsCoursesLed } from '../TeacherDetailsCoursesLed';
import { TeacherDetailsSubjects } from '../TeacherDetailsSubjects';
import { TeacherResponse } from '../../../dto/teacher';
import { useFetch } from '../../../hooks/useFetch';
import { useParams } from 'react-router-dom';

export const TeacherDetails = (): JSX.Element => {
  const { id } = useParams();
  const { data: teacher, loading, error } = useFetch<TeacherResponse>(`/api/teacher/${id}`);

  return (
    <div className={'teacher-details'}>
      {loading && <Loader style={{ width: '100%' }}/>}
      {error && <Alert icon={<IconAlertCircle size={16}/>} color="red">
        Nie udało się załadować danych nauczyciela
      </Alert>}

      {teacher &&
        <>
          <div className={'teacher-details-section'}>
            <Title order={2}>
              {`${teacher.academicTitle ?? ''} ${teacher.user.firstName} ${teacher.user.lastName}`.trim()}
            </Title>
          </div>
          <div className={'teacher-details-section'}>
            <Title order={3}>
              Prowadzone przedmioty
            </Title>
            <TeacherDetailsSubjects/>
          </div>
          <div className={'teacher-details-section'}>
            <Title order={3}>
              Wychowawstwa
            </Title>
            <TeacherDetailsCoursesLed teacherId={teacher.id}/>
          </div>
        </>}
    </div>
  );
};
