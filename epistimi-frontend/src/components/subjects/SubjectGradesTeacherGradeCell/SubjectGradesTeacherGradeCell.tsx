import './SubjectGradesTeacherGradeCell.scss';
import { ActionIcon } from '@mantine/core';
import { GradeResponse } from '../../../dto/grade';
import { IconPencil } from '@tabler/icons';
import { SubjectGradesTeacherGrade } from '../SubjectGradesTeacherGrade';

interface SubjectGradesTeacherGradeCellProps {
  grades: GradeResponse[];
}

export const SubjectGradesTeacherGradeCell = (
  { grades }: SubjectGradesTeacherGradeCellProps,
): JSX.Element => {
  return (
    <div className={'subject-teacher-grade-cell'}>
      {grades.map((grade) =>
        <SubjectGradesTeacherGrade key={grade.id} grade={grade}/>
      )}
      <div className={'subject-teacher-grade-cell-issue'}>
        <ActionIcon size={24} component={'button'}>
          <IconPencil size={16}/>
        </ActionIcon>
      </div>
    </div>
  );
};