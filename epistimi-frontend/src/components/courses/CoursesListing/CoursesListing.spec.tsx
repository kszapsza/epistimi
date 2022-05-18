import { CoursesListing } from './CoursesListing';
import { CoursesResponse } from '../../../dto/course';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('CoursesListing component', () => {
  it('should render courses listing grouped by school year (groups sorted descending)', async () => {
    axiosMock.get.mockResolvedValue({
      data: coursesResponse,
    });

    const { getAllByText } = render(<CoursesListing/>);

    await waitFor(() => {
      const groups = getAllByText(/\d{4}\/\d{4}/);
      expect(groups).toHaveLength(2);
      expect(groups[0]).toHaveTextContent(/2012\/2013/);
      expect(groups[1]).toHaveTextContent(/2011\/2012/);
    });
  });

  it('should render an error message if API fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(<CoursesListing/>);

    await waitFor(() => {
      const messageBox = getByText(/Nie udało się załadować listy klas!/);
      expect(messageBox).toBeInTheDocument();
    });
  });

  it('should render a message if there are no classes in organization', async () => {
    axiosMock.get.mockResolvedValue({
      data: {
        courses: [],
      } as CoursesResponse,
    });

    const { getByText } = render(<CoursesListing/>);

    await waitFor(() => {
      const messageBox = getByText(/W placówce nie zarejestrowano jeszcze żadnych klas!/);
      expect(messageBox).toBeInTheDocument();
    });
  });

  const coursesResponse: CoursesResponse = {
    courses: [
      {
        id: '2',
        code: {
          number: '5',
          letter: 'a',
        },
        schoolYear: '2011/2012',
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
        students: [],
        schoolYearBegin: new Date('2011-09-01'),
        schoolYearSemesterEnd: new Date('2012-01-22'),
        schoolYearEnd: new Date('2013-06-29'),
      },
      {
        id: '1',
        code: {
          number: '6',
          letter: 'a',
        },
        schoolYear: '2012/2013',
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
        students: [],
        schoolYearBegin: new Date('2012-09-03'),
        schoolYearSemesterEnd: new Date('2013-01-18'),
        schoolYearEnd: new Date('2013-06-28'),
      },
    ],
  };
});
