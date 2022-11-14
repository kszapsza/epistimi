import './SubjectGradesTeacherGrade.scss';
import { Badge, HoverCard } from '@mantine/core';
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
          color={'orange.6'} radius={'xs'} size={'md'} p={0} variant={'filled'}
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