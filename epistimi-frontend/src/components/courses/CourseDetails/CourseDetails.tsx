import './CourseDetails.scss';
import { Alert, Loader } from '@mantine/core';
import { AlertCircle } from 'tabler-icons-react';
import { AxiosError } from 'axios';
import { CourseResponse } from '../../../dto/course';
import { useFetch } from '../../../hooks/useFetch';
import { useParams } from 'react-router-dom';

export const CourseDetails = (): JSX.Element => {
  const { id } = useParams();

  const {
    data: course,
    // setData: setCourse,
    loading,
    error,
  } = useFetch<CourseResponse>(`/api/course/${id}`);

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
          {course.code.number}{course.code.letter}
        </div>
      }
    </>
  );
};
