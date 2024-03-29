import { CourseResponse } from '../../../dto/course';
import { CoursesListingGroup } from './CoursesListingGroup';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';

describe('CoursesListingGroup component', () => {
  it('should render course group listing sorted by course codes', () => {
    const { queryAllByRole } = render(<CoursesListingGroup courses={courses}/>);

    const links = queryAllByRole('link');

    expect(links).toHaveLength(2);
    expect(links[0]).toHaveTextContent(/1b/);
    expect(links[1]).toHaveTextContent(/2c/);
  });

  const classTeacher = {
    id: 'teacher_id',
    user: {
      id: 'teacher_user_id',
      firstName: 'Maciej',
      lastName: 'Nowak',
      role: UserRole.TEACHER,
      username: 'm.nowak',
    },
  };

  const courses: CourseResponse[] = [
    {
      id: '1',
      code: {
        number: '2',
        letter: 'c',
      },
      schoolYear: '2012/2013',
      classTeacher,
      students: [],
      subjects: [],
      schoolYearBegin: new Date('2012-09-03'),
      schoolYearSemesterEnd: new Date('2013-01-18'),
      schoolYearEnd: new Date('2013-06-28'),
    },
    {
      id: '2',
      code: {
        number: '1',
        letter: 'b',
      },
      schoolYear: '2012/2013',
      classTeacher,
      students: [],
      subjects: [],
      schoolYearBegin: new Date('2012-09-03'),
      schoolYearSemesterEnd: new Date('2013-01-18'),
      schoolYearEnd: new Date('2013-06-28'),
    },
  ];
});
