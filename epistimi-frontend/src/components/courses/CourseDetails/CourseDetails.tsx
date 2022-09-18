import './CourseDetails.scss';
import { ActionIcon, Alert, Button, Loader, Modal, Title } from '@mantine/core';
import { AxiosError } from 'axios';
import { CourseAddStudent, CourseDetailsData, CourseDetailsStudents } from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { IconAlertCircle, IconArrowBack, IconArrowBigUpLines, IconBook, IconSchool } from '@tabler/icons';
import { Link, useParams } from 'react-router-dom';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';

export const CourseDetails = (): JSX.Element => {
  const { id } = useParams();
  const {
    data: course,
    setData: setCourse,
    loading,
    error,
  } = useFetch<CourseResponse>(`/api/course/${id}`);

  const [addStudentModalOpened, addStudentModalHandlers] = useDisclosure(false);

  useDocumentTitle(course && `${course.code.number}${course.code.letter} (${course.schoolYear})`);

  const getErrorMessage = (error: AxiosError): string => {
    if (error.response?.status === 404) {
      return 'Nie znaleziono klasy';
    }
    return 'Nie udało się połączyć z serwerem';
  };

  return (<>
      {loading && <Loader style={{ width: '100%' }}/>}
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
          onStudentRegistered={(response) => {
            course && setCourse({
              ...course,
              students: [...course.students, {
                id: response.id,
                user: response.student.user,
                parents: response.parents.map((parentResponse) => parentResponse.parent),
              }],
            });
          }}
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
                variant={'default'}
              >
                Dodaj ucznia
              </Button>
              <Button
                leftIcon={<IconBook size={16}/>}
                // onClick={editModalHandlers.open}
                variant={'default'}
                disabled={true} // TODO
              >
                Dodaj przedmiot
              </Button>
              <Button
                leftIcon={<IconArrowBigUpLines size={16}/>}
                // onClick={editModalHandlers.open}
                variant={'default'}
                disabled={true} // TODO
              >
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
            <CourseDetailsData course={course}/>
          </div>

          <div className={'course-details-box'}>
            <Title order={4}>Uczniowie</Title> ({course.students.length})
            <CourseDetailsStudents students={course.students}/>
          </div>

          <div className={'course-details-box'}>
            <div>
              <Title order={4}>Przedmioty</Title>
            </div>
          </div>
        </div>
      }
    </>
  );
};
