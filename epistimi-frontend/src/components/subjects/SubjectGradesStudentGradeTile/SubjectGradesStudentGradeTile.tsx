import './SubjectGradesStudentGradeTile.scss';
import { createStyles, Paper } from '@mantine/core';
import { GradeBadgeDropdown } from '../../grades';
import { GradeResponse } from '../../../dto/grade';

const useStyles = createStyles(() => ({
  tile: {
    minWidth: '300px',
  },
}));

interface SubjectGradesStudentGradeTileProps {
  grade: GradeResponse;
}

export const SubjectGradesStudentGradeTile = (
  { grade }: SubjectGradesStudentGradeTileProps,
): JSX.Element => {
  const { classes } = useStyles();

  return (
    <Paper withBorder p={'md'} radius={'sm'} key={grade.id} className={classes.tile}>
      <GradeBadgeDropdown grade={grade}/>
    </Paper>
  );
};

