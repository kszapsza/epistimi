import './GradeBadgesTableCell.scss';
import { ActionIcon } from '@mantine/core';
import { GradeBadge } from '../GradeBadge';
import { GradeResponse } from '../../../dto/grade';
import { IconPlus } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface SubjectGradesTeacherGradesCellProps {
  grades: GradeResponse[];
  onIssueGradeClick?: () => void;
}

export const GradeBadgesTableCell = (
  { grades, onIssueGradeClick }: SubjectGradesTeacherGradesCellProps,
): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className={'grade-badges-table-cell'}>
      {grades.length === 0 &&
        <span className={'grade-badges-table-cell-no-grades'}>Brak ocen</span>}
      {grades.length > 0 && grades.map((grade) =>
        <GradeBadge key={grade.id} grade={grade}/>
      )}
      {user && user.role === UserRole.TEACHER && onIssueGradeClick && (
        <div className={'grade-badges-table-cell-issue'}>
          <ActionIcon
            size={24}
            component={'button'}
            onClick={onIssueGradeClick}
          >
            <IconPlus size={16}/>
          </ActionIcon>
        </div>)}
    </div>
  );
};