import './SubjectGradesTeacherGrade.scss';
import { Badge, HoverCard } from '@mantine/core';
import { determineTextColor } from '../../../utils/color-utils';
import { GradeResponse } from '../../../dto/grade';
import { SubjectGradesTeacherGradeDropdown } from '../SubjectGradesTeacherGradeDropdown';

interface SubjectGradesTeacherGradeBadgeProps {
  grade: GradeResponse;
}

export const SubjectGradesTeacherGrade = (
  { grade }: SubjectGradesTeacherGradeBadgeProps,
): JSX.Element => {
  return (
    <HoverCard width={300} shadow={'sm'}>
      <HoverCard.Target>
        <Badge className={'subject-grade-badge'}
               radius={'xs'} size={'md'} p={0}
               style={{
                 backgroundColor: grade.category.color ?? '#228be6',
                 color: grade.category.color ? determineTextColor(grade.category.color) : '#fff',
                 fontWeight: '600',
               }}
        >
          {grade.value.displayName}
        </Badge>
      </HoverCard.Target>
      <HoverCard.Dropdown mt={-1}>
        <SubjectGradesTeacherGradeDropdown grade={grade}/>
      </HoverCard.Dropdown>
    </HoverCard>
  );
};