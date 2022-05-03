import './CourseAddStudents.scss';
import { CourseResponse } from '../../../dto/course';

interface CourseAddStudentsProps {
  course: CourseResponse;
}

export const CourseAddStudents = (
  props: CourseAddStudentsProps
): JSX.Element => {
  return (
    <div className={'course-add-students'}>

    </div>
  );
};
