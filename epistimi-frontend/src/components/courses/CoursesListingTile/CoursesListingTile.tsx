import './CoursesListingTile.scss';
import { Box } from '@mantine/core';
import { CourseCode } from '../../../dto/course';
import { CoursesListingTileAvatars } from '../../courses';
import { Link } from 'react-router-dom';
import { StudentResponse } from '../../../dto/student';
import { TeacherResponse } from '../../../dto/teacher';
import { useTranslation } from 'react-i18next';

interface CoursesListingTileProps {
  id: string;
  code: CourseCode;
  classTeacher: TeacherResponse;
  students: StudentResponse[];
}

export const CoursesListingTile = (props: CoursesListingTileProps): JSX.Element => {
  const { t } = useTranslation();

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
        {t('courses.coursesListingTile.studentsCount', { count: props.students.length })}
      </div>
    </Box>
  );
};
