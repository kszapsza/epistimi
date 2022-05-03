import './CourseDetails.scss';
import { ActionIcon, Alert, Avatar, Button, Loader, Title } from '@mantine/core';
import { AlertCircle, ArrowBack, ArrowBigUpLines, School } from 'tabler-icons-react';
import { AxiosError } from 'axios';
import { CourseDetailsKeyValue } from '../CourseDetailsKeyValue';
import { CourseDetailsStudents } from '../CourseDetailsStudents';
import { CourseResponse } from '../../../dto/course';
import { Link, useParams } from 'react-router-dom';
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
        <Alert icon={<AlertCircle size={16}/>} color="red">
          {getErrorMessage(error)}
        </Alert>}

      {course &&
        <div className={'course-details'}>
          <div className={'course-actions'}>
            <ActionIcon variant={'transparent'} component={Link} to={'./..'}>
              <ArrowBack size={18}/>
            </ActionIcon>

            <div className={'course-action-group'}>
              <Button
                leftIcon={<School size={16}/>}
                // onClick={editModalHandlers.open}
                variant={'default'}>
                Dodaj uczniów
              </Button>
              <Button
                leftIcon={<ArrowBigUpLines size={16}/>}
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
        </div>
      }
    </>
  );
};
