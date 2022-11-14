import './SubjectHead.scss';
import { Link } from 'react-router-dom';
import { SubjectResponse } from '../../../dto/subject';
import { Title } from '@mantine/core';

interface SubjectHeadProps {
  subject: SubjectResponse;
}

export const SubjectHead = (props: SubjectHeadProps): JSX.Element => {
  const {
    subject: {
      name,
      teacher,
      course,
    },
  } = props;

  return (
    <div className={'subject-head'}>
      <div className={'subject-course'}>
        <Link to={`/courses/${course.id}`}>{course.code} ({course.schoolYear})</Link>
        &ensp;∙&ensp;{teacher.academicTitle} {teacher.user.lastName} {teacher.user.firstName}
      </div>
      <Title order={2} className={'subject-name'}>
        {name}
      </Title>
    </div>
  );
};