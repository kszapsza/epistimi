import './ClassificationGradeBadge.scss';
import { Badge, DEFAULT_THEME, HoverCard } from '@mantine/core';
import { ClassificationGradeBadgeDropdown } from '../ClassificationGradeBadgeDropdown';
import { StudentSubjectClassificationGradeResponse } from '../../../dto/student-grades';
import { SubjectStudentClassificationGradeResponse } from '../../../dto/subject-grades';

interface ClassificationGradeBadgeProps {
  grade?: SubjectStudentClassificationGradeResponse | StudentSubjectClassificationGradeResponse;
  header: string;
}

export const ClassificationGradeBadge = (
  { grade, header }: ClassificationGradeBadgeProps,
): JSX.Element => {
  if (!grade) {
    return <>—</>;
  }
  return (
    <HoverCard width={300} shadow={'sm'}>
      <HoverCard.Target>
        <Badge className={'grade-badge'}
               radius={'xs'} size={'md'} p={0}
               style={{
                 backgroundColor: DEFAULT_THEME.colors.blue[1],
                 color: '#000',
               }}
        >
          {grade.value.displayName}
        </Badge>
      </HoverCard.Target>
     <HoverCard.Dropdown mt={-1}>
        <ClassificationGradeBadgeDropdown grade={grade} header={header}/>
      </HoverCard.Dropdown>
    </HoverCard>
  );
};