import './CourseDetailsSubjects.scss';
import { CourseDetailsTile } from '../CourseDetailsTile';
import { CourseSubjectResponse } from '../../../dto/course';
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
      <div className={'course-no-subjects'}>
        {t('courses.courseDetailsSubjects.noSubjects')}
      </div>
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
