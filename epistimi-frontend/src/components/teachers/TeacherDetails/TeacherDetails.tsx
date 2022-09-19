import './TeacherDetails.scss';
import { Alert, Loader, Title } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { TeacherDetailsCoursesLed, TeacherDetailsSubjects } from '../../teachers';
import { TeacherResponse } from '../../../dto/teacher';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export const TeacherDetails = (): JSX.Element => {
  const { id } = useParams();
  const { data: teacher, loading, error } = useFetch<TeacherResponse>(`/api/teacher/${id}`);

  const { t } = useTranslation();
  useDocumentTitle(teacher && `${teacher.academicTitle ?? ''} ${teacher.user.firstName} ${teacher.user.lastName}`.trim());

  return (
    <div className={'teacher-details'}>
      {loading && <Loader style={{ width: '100%' }}/>}
      {error && <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
        {t('teachers.teacherDetails.couldNotLoad')}
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
              {t('teachers.teacherDetails.subjectsLed')}
            </Title>
            <TeacherDetailsSubjects/>
          </div>
          <div className={'teacher-details-section'}>
            <Title order={3}>
              {t('teachers.teacherDetails.coursesLed')}
            </Title>
            <TeacherDetailsCoursesLed teacherId={teacher.id}/>
          </div>
        </>}
    </div>
  );
};
