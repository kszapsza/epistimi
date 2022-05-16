import './CourseDetails.scss';
import { ActionIcon, Alert, Avatar, Button, Loader, Modal, Title } from '@mantine/core';
import { AxiosError } from 'axios';
import { CourseAddStudent } from '../CourseAddStudent';
import { CourseDetailsKeyValue } from '../CourseDetailsKeyValue';
import { CourseDetailsStudents } from '../CourseDetailsStudents';
import { CourseResponse } from '../../../dto/course';
import { IconAlertCircle, IconArrowBack, IconArrowBigUpLines, IconSchool } from '@tabler/icons';
import { Link, useParams } from 'react-router-dom';
import { useDisclosure } from '@mantine/hooks';
import { useEffect } from 'react';
import { useFetch } from '../../../hooks/useFetch';
import dayjs from 'dayjs';

export const CourseDetails = (): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY';

  const { id } = useParams();
  const {
    data: course,
    // setData: setCourse,
    loading,
    error,
  } = useFetch<CourseResponse>(`/api/course/${id}`);

  const [addStudentModalOpened, addStudentModalHandlers] = useDisclosure(false);

  useEffect(() => {
    course && (document.title = `${course.code.number}${course.code.letter} (${course.schoolYear}) – Epistimi`);
  }, [course]);

  const getErrorMessage = (error: AxiosError): string => {
    if (error.response?.status === 404) {
      return 'Nie znaleziono klasy';
    }
    return 'Nie udało się połączyć z&nbsp;serwerem';
  };

  return (<>
      {loading && <Loader/>}
      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color="red">
          {getErrorMessage(error)}
        </Alert>}

      {course && <Modal
        onClose={addStudentModalHandlers.close}
        opened={addStudentModalOpened}
        size={'xl'}
        title={'Dodawanie ucznia do klasy'}
      >
        <CourseAddStudent
          course={course}
        />
      </Modal>}

      {course &&
        <div className={'course-details'}>
          <div className={'course-actions'}>
            <ActionIcon variant={'transparent'} component={Link} to={'./..'}>
              <IconArrowBack size={18}/>
            </ActionIcon>

            <div className={'course-action-group'}>
              <Button
                leftIcon={<IconSchool size={16}/>}
                onClick={addStudentModalHandlers.open}
                variant={'default'}>
                Dodaj ucznia
              </Button>
              <Button
                leftIcon={<IconArrowBigUpLines size={16}/>}
                // onClick={editModalHandlers.open}
                variant={'default'}>
                Promocja klasy
              </Button>
            </div>
          </div>


          <div className={'course-details-box'}>
            <div className={'course-school-year'}>
              {course.schoolYear}
            </div>
            <div className={'course-code'}>
              {course.code.number}{course.code.letter}
            </div>
          </div>

          <div className={'course-details-box'}>
            <Title order={4}>Dane</Title>
            <CourseDetailsKeyValue
              label={'Wychowawca klasy'}
              value={
                <div className={'course-class-teacher'}>
                  <Avatar size={'xs'} radius={'xl'} color={'orange'}>
                    {course.classTeacher.user.firstName[0]}{course.classTeacher.user.lastName[0]}
                  </Avatar>
                  {course.classTeacher.user.firstName} {course.classTeacher.user.lastName}
                </div>
              }/>

            {course.profile && <CourseDetailsKeyValue
              label={'Profil'}
              value={course.profile}/>}
            {course.profession && <CourseDetailsKeyValue
              label={'Zawód'}
              value={course.profession}/>}
            {course.specialization && <CourseDetailsKeyValue
              label={'Specjalizacja'}
              value={course.specialization}/>}

            <CourseDetailsKeyValue
              label={'Rozpoczęcie roku'}
              value={dayjs(course.schoolYearBegin).format(DATE_FORMAT)}/>
            <CourseDetailsKeyValue
              label={'Koniec pierwszego semestru'}
              value={dayjs(course.schoolYearSemesterEnd).format(DATE_FORMAT)}/>
            <CourseDetailsKeyValue
              label={'Koniec roku'}
              value={dayjs(course.schoolYearEnd).format(DATE_FORMAT)}/>
          </div>

          <div className={'course-details-box'}>
            <Title order={4}>Uczniowie</Title>
            <CourseDetailsStudents students={course.students}/>
          </div>

          <div className={'course-details-box'}>
            <Title order={4}>Statystyki</Title>
          </div>
        </div>
      }
    </>
  );
};
