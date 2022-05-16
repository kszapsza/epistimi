import './CourseDetailsStudents.scss';
import { StudentResponse } from '../../../dto/student';

interface CourseDetailsStudentsProps {
  students: StudentResponse[];
}

export const CourseDetailsStudents = (
  { students }: CourseDetailsStudentsProps,
): JSX.Element => {
  if (students.length === 0) {
    return (
      <div className={'course-no-students'}>
        Brak uczniów
      </div>
    );
  }

  return (
    <div className={'course-students'}>
      {students.map((student, idx) => (
        <div className={'course-student'} key={idx}>
          <div>{idx + 1}</div>
          <div>{student.user.firstName} {student.user.lastName}</div>
        </div>
      ))}
    </div>
  );
};
