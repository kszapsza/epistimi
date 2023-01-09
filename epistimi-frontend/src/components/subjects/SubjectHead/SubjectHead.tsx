import './SubjectHead.scss';
import { Link } from 'react-router-dom';
import { SubjectAvatar } from '../../common';
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
      <SubjectAvatar subjectName={name} size={'lg'}/>
      <div>
        <div className={'subject-course'}>
          <Link to={`/app/courses/${course.id}`}>{course.code} ({course.schoolYear})</Link>
          &ensp;∙&ensp;{teacher.academicTitle} {teacher.user.lastName} {teacher.user.firstName}
        </div>
        <Title order={2} className={'subject-name'}>
          {name}
        </Title>
      </div>
    </div>
  );
};