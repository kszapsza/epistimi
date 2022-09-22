import { CoursesListingTile } from './CoursesListingTile';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';

describe('CoursesListingTile component', () => {

  const STUDENTS_COUNT_REGEXP = /courses\.coursesListingTile\.studentsCount/;

  it('should render component', () => {
    const { queryByText } = render(<CoursesListingTile {...testProps}/>);

    expect(queryByText('7b')).toBeInTheDocument();
    expect(queryByText('Maciej Nowak')).toBeInTheDocument();
    expect(queryByText(STUDENTS_COUNT_REGEXP)).toBeInTheDocument();
  });

  const testStudent = {
    id: 'student_id',
    user: {
      id: 'student_user_id',
      firstName: 'Adam',
      lastName: 'Kowalski',
      role: UserRole.STUDENT,
      username: 'a.kowalski',
    },
    parents: [],
  };

  const testProps = {
    id: 'course_id',
    code: {
      number: '7',
      letter: 'b',
    },
    classTeacher: {
      id: 'teacher_id',
      user: {
        id: 'teacher_user_id',
        firstName: 'Maciej',
        lastName: 'Nowak',
        role: UserRole.TEACHER,
        username: 'm.nowak',
      },
    },
    students: Array(10).fill(testStudent),
  };
});
