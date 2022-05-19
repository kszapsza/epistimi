import './CourseDetailsStudents.scss';
import { Avatar, Card } from '@mantine/core';
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
        <Card key={idx} className={'course-student'}>
          <Avatar size={'sm'} radius={'xl'} color={'green'}>
            {`${student.user.firstName[0]}${student.user.lastName[0]}`.toUpperCase()}
          </Avatar>
          <div className={'course-student-name'}>
            {student.user.firstName} {student.user.lastName}
          </div>
        </Card>
      ))}
    </div>
  );
};
