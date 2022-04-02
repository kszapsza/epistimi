import './CoursesListing.scss';
import { Accordion, Alert } from '@mantine/core';
import { AlertCircle, Loader } from 'tabler-icons-react';
import { CourseResponse, CoursesResponse } from '../../../dto/course';
import { CoursesListingGroup } from '../CoursesListingGroup';
import { useFetch } from '../../../hooks/useFetch';

export const CoursesListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<CoursesResponse>('api/course');

  const coursesBySchoolYear = data?.courses
    .reduce((acc, next) => {
      if (!acc[next.schoolYear]) {
        acc[next.schoolYear] = [next];
      } else {
        acc[next.schoolYear] = [...acc[next.schoolYear], next];
      }
      return acc;
    }, {} as { [schoolYear: string]: CourseResponse[] });

  return (
    <div className={'courses'}>
      <h2>Klasy</h2>
      {loading && <Loader/>}
      {error &&
        <Alert icon={<AlertCircle size={16}/>} color="red">
          Nie udało się załadować listy klas!
        </Alert>}
      {coursesBySchoolYear &&
        <Accordion initialItem={0}>
          {Object.entries(coursesBySchoolYear)
            .sort(([schoolYearA], [schoolYearB]) =>
              schoolYearB.localeCompare(schoolYearA, 'pl-PL'))
            .map(([schoolYear, courses]) =>
              <Accordion.Item label={schoolYear} key={schoolYear}>
                <CoursesListingGroup courses={courses}/>
              </Accordion.Item>,
            )
          }
        </Accordion>
      }
    </div>
  );
};
