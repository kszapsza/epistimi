import './CoursesListingTile.scss';
import { Box, ThemeIcon } from '@mantine/core';
import { Code } from '../../../dto/course';
import { Link } from 'react-router-dom';
import { TeacherResponse } from '../../../dto/teacher';
import { Users } from 'tabler-icons-react';

interface CoursesListingTileProps {
  id: string;
  code: Code;
  classTeacher: TeacherResponse;
  studentsCount: number;
}

export const CoursesListingTile = (props: CoursesListingTileProps): JSX.Element => {
  const getStudentsPluralForm = (n: number) => {
    if (n === 1) {
      return `${n} uczeń`;
    } else {
      return `${n} uczniów`;
    }
  };

  return (
    <Box className={'course-tile'} component={Link} to={`/app/courses/${props.id}`} role={'link'}>
      <div className={'course-tile-icon'}>
        <ThemeIcon variant={'light'} color={'blue'} size={'lg'}>
          <Users/>
        </ThemeIcon>
      </div>
      <div className={'course-tile-name'}>
        {props.code.number}{props.code.letter}
      </div>
      <div className={'course-tile-class-teacher'}>
        {props.classTeacher.user.firstName} {props.classTeacher.user.lastName}
      </div>
      <div className={'course-tile-students-count'}>
        {getStudentsPluralForm(props.studentsCount)}
      </div>
    </Box>
  );
};
