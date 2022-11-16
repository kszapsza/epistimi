import './SubjectGradesTeacherGradesCell.scss';
import { ActionIcon } from '@mantine/core';
import { GradeResponse } from '../../../dto/grade';
import { IconPlus } from '@tabler/icons';
import { SubjectGradesTeacherGrade } from '../SubjectGradesTeacherGrade';

interface SubjectGradesTeacherGradesCellProps {
  grades: GradeResponse[];
  onIssueGradeClick: () => void;
}

export const SubjectGradesTeacherGradesCell = (
  { grades, onIssueGradeClick }: SubjectGradesTeacherGradesCellProps,
): JSX.Element => {
  return (
    <div className={'subject-teacher-grade-cell'}>
      {grades.map((grade) =>
        <SubjectGradesTeacherGrade key={grade.id} grade={grade}/>
      )}
      <div className={'subject-teacher-grade-cell-issue'}>
        <ActionIcon size={24} component={'button'} onClick={onIssueGradeClick}>
          <IconPlus size={16}/>
        </ActionIcon>
      </div>
    </div>
  );
};