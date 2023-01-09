import './CourseDetailsSubjects.scss';
import { Alert } from '@mantine/core';
import { CourseDetailsTile } from '../CourseDetailsTile';
import { CourseSubjectResponse } from '../../../dto/course';
import { IconInfoCircle } from '@tabler/icons';
import { SubjectAvatar } from '../../common';
import { useTranslation } from 'react-i18next';

interface CourseDetailsSubjectsProps {
  subjects: CourseSubjectResponse[];
}

export const CourseDetailsSubjects = (
  { subjects }: CourseDetailsSubjectsProps
): JSX.Element => {

  const { t } = useTranslation();

  if (subjects.length === 0) {
    return (
      <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
        {t('courses.courseDetailsSubjects.noSubjects')}
      </Alert>
    );
  }

  const subjectCompareFn = (a: CourseSubjectResponse, b: CourseSubjectResponse) => {
    return a.name.localeCompare(b.name, 'pl-PL');
  };

  return (
    <div className={'course-subjects'}>
      {subjects
        .sort(subjectCompareFn)
        .map(({ id, name, teacher }) => (
          <CourseDetailsTile
            key={id}
            avatar={<SubjectAvatar subjectName={name} size={'md'}/>}
            href={`/app/subjects/${id}`}
            title={name}
            subtitle={`${teacher.lastName} ${teacher.firstName}`}
          />
        ))}
    </div>
  );
};
