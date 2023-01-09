import './GradeBadgeDropdown.scss';
import { Badge } from '@mantine/core';
import { determineTextColor } from '../../../utils/color-utils';
import { GradeBadgeDropdownEntries } from '../GradeBadgeDropdownEntries';
import { GradeResponse } from '../../../dto/grade';

interface SubjectGradesTeacherGradeDropdownProps {
  grade: GradeResponse;
}

export const GradeBadgeDropdown = (
  { grade }: SubjectGradesTeacherGradeDropdownProps,
): JSX.Element => {
  return (
    <div className={'grade-dropdown'}>
      <div className={'grade-dropdown-head'}>
        <Badge
          radius={'xs'} size={'xl'} p={0}
          style={{
            backgroundColor: grade.category.color ?? '#228be6',
            color: grade.category.color ? determineTextColor(grade.category.color) : '#fff',
            fontWeight: '600',
            width: '36px',
            height: '36px',
          }}
        >
          {grade.value.displayName}
        </Badge>
        <div className={'grade-dropdown-head-text'}>
          <div className={'grade-dropdown-head-category'}>
            {grade.category.name}
          </div>
          <div className={'grade-dropdown-head-value'}>
            {grade.value.fullName}
          </div>
        </div>
      </div>
      <GradeBadgeDropdownEntries grade={grade}/>
    </div>
  );
};
