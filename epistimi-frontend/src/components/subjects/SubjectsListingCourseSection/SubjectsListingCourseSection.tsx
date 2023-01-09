import './SubjectsListingCourseSection.scss';
import { Badge, Card, Text } from '@mantine/core';
import { CourseSubjectsResponse } from '../../../dto/subject-multi';
import { IconBook, IconUsers } from '@tabler/icons';
import { Link } from 'react-router-dom';
import { SubjectAvatar } from '../../common';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface SubjectsListingCourseSectionProps {
  courseSubjectsResponse: CourseSubjectsResponse;
}

export const SubjectsListingCourseSection = (
  { courseSubjectsResponse: { classTeacher, code, studentsCount, subjects } }: SubjectsListingCourseSectionProps,
): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className={'subject-listing-course-section'}>
      <div className={'subject-listing-course-tile'}>
        <div className={'subject-listing-course-text'}>
          <Text weight={'bold'} size={'lg'}>
            {code}
          </Text>
          <Text size={'xs'}>
            {classTeacher.user.lastName} {classTeacher.user.firstName}
          </Text>
        </div>
        <div className={'subject-listing-course-badges'}>
          <Badge leftSection={<IconUsers size={10}/>}>{studentsCount}</Badge>
          <Badge leftSection={<IconBook size={10}/>}>{subjects.length}</Badge>
        </div>
      </div>
      <div className={'subject-listing-subject-tiles'}>
        {subjects.map(({ id, name, teacher }) => (
          <Card withBorder className={'subject-listing-subject-tile'} key={id}
                component={Link} to={`/app/subjects/${id}`} role={'link'}>
            <SubjectAvatar subjectName={name} size={'md'}/>
            <div className={'subject-listing-subject-tile-text'}>
              <div className={'subject-listing-subject-tile-title'}>
                {name}
              </div>
              {user && user.role !== UserRole.TEACHER && (
                <div className={'subject-listing-subject-tile-teacher'}>
                  {`${teacher.academicTitle ?? ''} ${teacher.user.lastName} ${teacher.user.firstName}`.trim()}
                </div>
              )}</div>
          </Card>
        ))}
      </div>
    </div>
  );
};

