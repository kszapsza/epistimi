import './CoursesListing.scss';
import { Accordion, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { AlertCircle, InfoCircle, Pencil } from 'tabler-icons-react';
import { CourseEdit } from '../CourseEdit';
import { CourseResponse, CoursesResponse } from '../../../dto/course';
import { CoursesListingGroup } from '../CoursesListingGroup';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks/useFetch';

export const CoursesListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<CoursesResponse>('api/course');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);

  const coursesBySchoolYear = data && Object.entries(
    data.courses
      .reduce((acc, next) => {
        if (!acc[next.schoolYear]) {
          acc[next.schoolYear] = [next];
        } else {
          acc[next.schoolYear] = [...acc[next.schoolYear], next];
        }
        return acc;
      }, {} as { [schoolYear: string]: CourseResponse[] }),
  );

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={'Utwórz nową klasę'}
      >
        <CourseEdit/>
      </Modal>
      <div className={'courses'}>
        <div className={'courses-actions'}>
          <Title order={2}>Klasy</Title>
          <Button
            leftIcon={<Pencil size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            Utwórz nową
          </Button>
        </div>

        {loading && <Loader/>}
        {error &&
          <Alert icon={<AlertCircle size={16}/>} color="red">
            Nie udało się załadować listy klas!
          </Alert>}
        {coursesBySchoolYear?.length === 0 &&
          <Alert icon={<InfoCircle size={16}/>} color="blue">
            W placówce nie zarejestrowano jeszcze żadnych klas!
          </Alert>
        }

        {coursesBySchoolYear && coursesBySchoolYear?.length > 0 &&
          <Accordion initialItem={0}>
            {coursesBySchoolYear
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
    </>
  );
};
