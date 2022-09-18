import './CoursesListing.scss';
import { Accordion, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { CourseEdit, CoursesListingGroup } from '../../courses';
import { CourseResponse, CoursesResponse } from '../../../dto/course';
import { IconAlertCircle, IconCheck, IconInfoCircle, IconPlus } from '@tabler/icons';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';

export const CoursesListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<CoursesResponse>('api/course');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);

  useDocumentTitle('Klasy');

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

  const onCourseCreate = (course: CourseResponse): void => {
    data?.courses.push(course);
    createModalHandlers.close();
    createdMessageHandlers.open();
  };

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={'Utwórz nową klasę'}
      >
        <CourseEdit submitCallback={onCourseCreate}/>
      </Modal>
      <div className={'courses'}>
        <div className={'courses-actions'}>
          <Title order={2}>Klasy</Title>
          <Button
            leftIcon={<IconPlus size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            Utwórz nową
          </Button>
        </div>

        {loading && <Loader style={{ width: '100%' }}/>}
        {createdMessageOpened &&
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            Pomyślnie utworzono nową klasę
          </Alert>}
        {error &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            Nie udało się załadować listy klas!
          </Alert>}
        {coursesBySchoolYear?.length === 0 &&
          <Alert icon={<IconInfoCircle size={16}/>} color="blue">
            W placówce nie zarejestrowano jeszcze żadnych klas!
          </Alert>}

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
