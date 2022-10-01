import './CoursesListing.scss';
import { Accordion, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { CourseEdit, CoursesListingGroup } from '../../courses';
import { CourseResponse, CoursesResponse } from '../../../dto/course';
import { IconAlertCircle, IconCheck, IconInfoCircle, IconPlus } from '@tabler/icons';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const CoursesListing = (): JSX.Element => {
  const { data, loading, error } = useFetch<CoursesResponse>('api/course');
  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);

  const { t } = useTranslation();
  useDocumentTitle(t('courses.coursesListing.courses'));

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
        title={t('courses.coursesListing.createNewCourseModalTitle')}
      >
        <CourseEdit submitCallback={onCourseCreate}/>
      </Modal>
      <div className={'courses'}>
        <div className={'courses-actions'}>
          <Title order={2}>{t('courses.coursesListing.courses')}</Title>
          <Button
            leftIcon={<IconPlus size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            {t('courses.coursesListing.createNewCourseButton')}
          </Button>
        </div>

        {loading && <Loader style={{ width: '100%' }}/>}
        {createdMessageOpened &&
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t('courses.coursesListing.createdNewCourse')}
          </Alert>}
        {error &&
          <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
            {t('courses.coursesListing.couldNotLoadCourses')}
          </Alert>}
        {coursesBySchoolYear?.length === 0 &&
          <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
            {t('courses.coursesListing.noCoursesRegistered')}
          </Alert>}

        {/* TODO: expand first accordion as it was before Mantine UI bump */}
        {coursesBySchoolYear && coursesBySchoolYear?.length > 0 &&
          <Accordion>
            {coursesBySchoolYear
              .sort(([schoolYearA], [schoolYearB]) =>
                schoolYearB.localeCompare(schoolYearA, 'pl-PL'))
              .map(([schoolYear, courses]) => (
                  <Accordion.Item key={schoolYear} value={schoolYear}>
                    <Accordion.Control>
                      <strong>{schoolYear}</strong>
                    </Accordion.Control>
                    <Accordion.Panel>
                      <CoursesListingGroup courses={courses}/>
                    </Accordion.Panel>
                  </Accordion.Item>
                ),
              )
            }
          </Accordion>
        }
      </div>
    </>
  );
};
