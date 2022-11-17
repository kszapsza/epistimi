import './GradeBadge.scss';
import { Badge, DEFAULT_THEME, HoverCard } from '@mantine/core';
import { determineTextColor } from '../../../utils/color-utils';
import { GradeBadgeDropdown } from '../GradeBadgeDropdown';
import { GradeResponse } from '../../../dto/grade';

interface SubjectGradesTeacherGradeBadgeProps {
  grade: GradeResponse;
}

export const GradeBadge = (
  { grade }: SubjectGradesTeacherGradeBadgeProps,
): JSX.Element => {
  return (
    <HoverCard width={300} shadow={'sm'}>
      <HoverCard.Target>
        <Badge className={'grade-badge'}
               radius={'xs'} size={'md'} p={0}
               style={{
                 backgroundColor: grade.category.color ?? DEFAULT_THEME.colors.blue[6],
                 color: grade.category.color ? determineTextColor(grade.category.color) : '#fff',
               }}
        >
          {grade.value.displayName}
        </Badge>
      </HoverCard.Target>
      <HoverCard.Dropdown mt={-1}>
        <GradeBadgeDropdown grade={grade}/>
      </HoverCard.Dropdown>
    </HoverCard>
  );
};