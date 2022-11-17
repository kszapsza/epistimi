import './GradeBadgeDropdown.scss';
import { ActionIcon, Badge } from '@mantine/core';
import { determineTextColor } from '../../../utils/color-utils';
import { GradeBadgeDropdownEntries } from '../GradeBadgeDropdownEntries';
import { GradeResponse } from '../../../dto/grade';
import { IconPencil } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface SubjectGradesTeacherGradeDropdownProps {
  grade: GradeResponse;
  showActions?: boolean;
}

export const GradeBadgeDropdown = (
  { grade, showActions }: SubjectGradesTeacherGradeDropdownProps,
): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

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
        {user && user.role == UserRole.TEACHER && showActions !== false && (
          <div className={'grade-dropdown-head-actions'}>
            <ActionIcon size={24}>
              <IconPencil size={16}/>
            </ActionIcon>
          </div>)}
      </div>
      <GradeBadgeDropdownEntries grade={grade}/>
    </div>
  );
};
