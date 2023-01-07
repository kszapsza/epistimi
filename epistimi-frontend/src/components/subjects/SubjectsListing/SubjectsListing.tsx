import './SubjectsListing.scss';
import { Accordion, Alert, Badge, Text, Title } from '@mantine/core';
import { IconAlertCircle, IconBook, IconInfoCircle } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { SubjectsListingCourseSection } from '../SubjectsListingCourseSection';
import { SubjectsResponse } from '../../../dto/subject-multi';
import { useAppSelector } from '../../../store/hooks';
import { useFetch } from '../../../hooks';
import { UserRole } from '../../../dto/user';
import { useTranslation } from 'react-i18next';

export const SubjectsListing = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { data, loading, error } = useFetch<SubjectsResponse>('/api/subject');
  const { t } = useTranslation();

  return (
    <div className={'subjects-listing'}>
      <Title order={2}>
        {user && (user.role == UserRole.STUDENT || user.role == UserRole.TEACHER)
          ? 'Twoje przedmioty'
          : 'Przedmioty'}
      </Title>

      {loading && <LoaderBox/>}
      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={'Błąd'}>
          Wystąpił błąd podczas pobierania listy przedmiotów
        </Alert>}

      {data?.schoolYears?.length === 0 &&
        <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
          {t('subjects.subjectsListing.noSubjects')}
        </Alert>}

      {data && data.schoolYears.length > 0 && (
        <Accordion defaultValue={data.schoolYears[0].schoolYear}>
          {data.schoolYears.map(({ schoolYear, courses }) => (
            <Accordion.Item key={schoolYear} value={schoolYear}>
              <Accordion.Control>
                <Text weight={600}>
                  {schoolYear}
                  &ensp;
                  <Badge leftSection={<IconBook size={10}/>} color={'blue'} variant={'filled'}>
                    {courses.flatMap((course) => course.subjects).length}
                  </Badge>
                </Text>
              </Accordion.Control>
              <Accordion.Panel>
                {courses.map((course) => (
                  <SubjectsListingCourseSection key={course.courseId} courseSubjectsResponse={course}/>
                ))}
              </Accordion.Panel>
            </Accordion.Item>
          ))}
        </Accordion>
      )}
    </div>
  );
};

