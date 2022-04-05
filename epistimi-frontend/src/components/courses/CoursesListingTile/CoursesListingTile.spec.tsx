import { CoursesListingTile } from './CoursesListingTile';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';

describe('CoursesListingTile component', () => {
  it('should render component', () => {
    const { queryByText } = render(<CoursesListingTile {...testProps}/>);

    expect(queryByText('7b')).toBeInTheDocument();
    expect(queryByText('Maciej Nowak')).toBeInTheDocument();
    expect(queryByText('10 uczniów')).toBeInTheDocument();
  });

  it.each([
    [0, '0 uczniów'],
    [1, '1 uczeń'],
    [2, '2 uczniów'],
    [21, '21 uczniów'],
    [20, '20 uczniów'],
    [100, '100 uczniów'],
  ])('should properly format students plural form (%s student(s))', (
    count: number,
    formattedCount: string,
  ) => {
    const props = {
      ...testProps,
      students: Array(count),
    };

    const { queryByText } = render(<CoursesListingTile {...props}/>);

    expect(queryByText(formattedCount)).toBeInTheDocument();
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
