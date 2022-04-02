import './CoursesListingTile.scss';
import { Code } from '../../../dto/course';
import { TeacherResponse } from '../../../dto/teacher';
import { ThemeIcon } from '@mantine/core';
import { Users } from 'tabler-icons-react';

interface CoursesListingTileProps {
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
    <div className={'course-tile'}>
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
    </div>
  );
};
