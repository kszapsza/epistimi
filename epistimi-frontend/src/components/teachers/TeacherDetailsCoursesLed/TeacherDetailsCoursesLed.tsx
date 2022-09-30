import './TeacherDetailsCoursesLed.scss';
import { Alert, Card, Loader } from '@mantine/core';
import { CoursesResponse } from '../../../dto/course';
import { IconAlertCircle } from '@tabler/icons';
import { useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

interface TeacherDetailsCoursesLedProps {
  teacherId: string;
}

export const TeacherDetailsCoursesLed = (
  { teacherId }: TeacherDetailsCoursesLedProps
): JSX.Element => {
  const { t } = useTranslation();
  const { data, loading, error } = useFetch<CoursesResponse>(`/api/course?classTeacherId=${teacherId}`);

  return (
    <div className={'teacher-details-courses'}>
      {loading && <Loader style={{ width: '100%' }}/>}
      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
          {t('teachers.teacherDetailsCoursesLed.couldNotLoad')}
        </Alert>}

      {data && data.courses.length === 0 &&
        <div className={'teacher-details-no-courses'}>
          {t('teachers.teacherDetailsCoursesLed.noCourses')}
        </div>
      }

      {data && data.courses.length > 0 && data.courses.map((course) =>
        <Card className={'teacher-details-course'} component={'a'} href={`/app/courses/${course.id}`}>
          <div className={'teacher-details-course-code'}>
            {course.code.number}{course.code.letter}
          </div>
          <div className={'teacher-details-course-school-year'}>
            ({course.schoolYear})
          </div>
        </Card>)}
    </div>
  );
};
