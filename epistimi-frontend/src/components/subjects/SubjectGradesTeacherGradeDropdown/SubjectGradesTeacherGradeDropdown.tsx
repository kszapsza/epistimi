import './SubjectGradesTeacherGradeDropdown.scss';
import { Badge } from '@mantine/core';
import { GradeResponse } from '../../../dto/grade';
import dayjs from 'dayjs';

interface SubjectGradesTeacherGradeDropdownProps {
  grade: GradeResponse;
}

export const SubjectGradesTeacherGradeDropdown = (
  { grade }: SubjectGradesTeacherGradeDropdownProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY, HH:mm';

  return (
    <div className={'subject-grade-dropdown'}>
      <div className={'subject-grade-dropdown-head'}>
        <Badge
          color={'orange.6'} radius={'xs'} size={'xl'} p={0} variant={'light'}
          style={{ width: '36px', height: '36px' }}
        >
          {grade.value.displayName}
        </Badge>
        <div className={'subject-grade-dropdown-head-text'}>
          <div className={'subject-grade-dropdown-head-category'}>{grade.category.name}</div>
          <div className={'subject-grade-dropdown-head-value'}>{grade.value.fullName}</div>
        </div>
      </div>

      <div className={'subject-grade-dropdown-meta'}>
        <div className={'subject-grade-dropdown-meta-entry'}>
          <div className={'subject-grade-dropdown-meta-key'}>
            Waga
          </div>
          <div className={'subject-grade-dropdown-meta-value'}>
            {grade.weight}
          </div>
        </div>
        <div className={'subject-grade-dropdown-meta-entry'}>
          <div className={'subject-grade-dropdown-meta-key'}>
            Nauczyciel
          </div>
          <div className={'subject-grade-dropdown-meta-value'}>
            {`${grade.issuedBy.academicTitle} ${grade.issuedBy.lastName} ${grade.issuedBy.firstName}`.trim()}
          </div>
        </div>
        <div className={'subject-grade-dropdown-meta-entry'}>
          <div className={'subject-grade-dropdown-meta-key'}>
            Wystawiono
          </div>
          <div className={'subject-grade-dropdown-meta-value'}>
            {dayjs(grade.issuedAt).format(DATE_FORMAT)}
          </div>
        </div>
        {grade.updatedAt && (
          <div className={'subject-grade-dropdown-meta-entry'}>
            <div className={'subject-grade-dropdown-meta-key'}>
              Zaktualizowano
            </div>
            <div className={'subject-grade-dropdown-meta-value'}>
              {dayjs(grade.updatedAt).format(DATE_FORMAT)}
            </div>
          </div>)}
        <div className={'subject-grade-dropdown-meta-entry'}>
          <div className={'subject-grade-dropdown-meta-key'}>
            Licz do średniej
          </div>
          <div className={'subject-grade-dropdown-meta-value'}>
            {grade.countTowardsAverage ? 'TAK' : 'NIE'}
          </div>
        </div>
        {grade.comment && (
          <div className={'subject-grade-dropdown-meta-entry'}>
            <div className={'subject-grade-dropdown-meta-key'}>
              Komentarz
            </div>
            <div className={'subject-grade-dropdown-meta-value'}>
              {grade.comment}
            </div>
          </div>)}
      </div>
    </div>
  );
};
