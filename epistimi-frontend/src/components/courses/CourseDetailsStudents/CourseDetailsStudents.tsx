import './CourseDetailsStudents.scss';
import { CourseDetailsTile } from '../CourseDetailsTile';
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

  const studentFullName = ({ user: { firstName, lastName } }: StudentResponse) => {
    return `${lastName} ${firstName}`;
  };

  const studentCompareFn = (a: StudentResponse, b: StudentResponse) => {
    return studentFullName(a).localeCompare(studentFullName(b), 'pl-PL');
  };

  return (
    <div className={'course-students'}>
      {students
        .sort(studentCompareFn)
        .map(({ id, user }) => (
          <CourseDetailsTile
            key={id}
            avatar={<UserAvatar user={user} size={'md'} radius={'xl'}/>}
            href={`/app/students/${id}`}
            title={`${user.lastName} ${user.firstName}`}
            subtitle={user.username}
          />
        ))}
    </div>
  );
};
