import './ClassificationGradeBadgeDropdown.scss';
import { Badge, DEFAULT_THEME } from '@mantine/core';
import { ClassificationGradeBadgeDropdownEntries } from '../ClassificationGradeBadgeDropdownEntries';
import { StudentSubjectClassificationGradeResponse } from '../../../dto/student-grades';
import { SubjectStudentClassificationGradeResponse } from '../../../dto/subject-grades';

interface ClassificationGradeBadgeDropdownProps {
  grade: SubjectStudentClassificationGradeResponse | StudentSubjectClassificationGradeResponse;
  header: string;
}

export const ClassificationGradeBadgeDropdown = (
  { grade, header }: ClassificationGradeBadgeDropdownProps,
): JSX.Element => {
  return (
    <div className={'grade-dropdown'}>
      <div className={'grade-dropdown-head'}>
        <Badge
          radius={'xs'} size={'xl'} p={0}
          style={{
            backgroundColor: DEFAULT_THEME.colors.blue[1],
            color: '#000',
            fontWeight: '600',
            width: '36px',
            height: '36px',
          }}
        >
          {grade.value.displayName}
        </Badge>
        <div className={'grade-dropdown-head-text'}>
          <div className={'grade-dropdown-head-category'}>
            {header}
          </div>
          <div className={'grade-dropdown-head-value'}>
            {grade.value.fullName}
          </div>
        </div>
      </div>
      <ClassificationGradeBadgeDropdownEntries grade={grade}/>
    </div>
  );
};
