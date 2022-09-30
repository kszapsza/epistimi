import './CourseDetailsStudents.scss';
import { Card } from '@mantine/core';
import { StudentResponse } from '../../../dto/student';
import { UserAvatar } from '../../common';
import { useTranslation } from 'react-i18next';

interface CourseDetailsStudentsProps {
  students: StudentResponse[];
}

export const CourseDetailsStudents = (
  { students }: CourseDetailsStudentsProps,
): JSX.Element => {
  const { t } = useTranslation();

  if (students.length === 0) {
    return (
      <div className={'course-no-students'}>
        {t('courses.courseDetailsStudents.noStudents')}
      </div>
    );
  }

  return (
    <div className={'course-students'}>
      {students.map((student, idx) => (
        <Card key={idx} className={'course-student'} component={'a'} href={`/app/students/${student.id}`}>
          <UserAvatar user={student.user} size={'sm'} radius={'xl'}/>
          <div className={'course-student-name'}>
            {student.user.firstName} {student.user.lastName}
          </div>
        </Card>
      ))}
    </div>
  );
};
