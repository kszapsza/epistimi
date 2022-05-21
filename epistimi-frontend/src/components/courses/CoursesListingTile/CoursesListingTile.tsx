import './CoursesListingTile.scss';
import { Box } from '@mantine/core';
import { CourseCode } from '../../../dto/course';
import { CoursesListingTileAvatars } from '../CoursesListingTileAvatars';
import { Link } from 'react-router-dom';
import { StudentResponse } from '../../../dto/student';
import { TeacherResponse } from '../../../dto/teacher';

interface CoursesListingTileProps {
  id: string;
  code: CourseCode;
  classTeacher: TeacherResponse;
  students: StudentResponse[];
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
        <CoursesListingTileAvatars students={props.students}/>
      </div>
      <div className={'course-tile-name'}>
        {props.code.number}{props.code.letter}
      </div>
      <div className={'course-tile-class-teacher'}>
        {props.classTeacher.user.firstName} {props.classTeacher.user.lastName}
      </div>
      <div className={'course-tile-students-count'}>
        {getStudentsPluralForm(props.students.length)}
      </div>
    </Box>
  );
};
