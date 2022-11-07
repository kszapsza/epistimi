import './CourseDetails.scss';
import { ActionIcon, Alert, Button, Loader, Modal } from '@mantine/core';
import { AxiosError } from 'axios';
import { CourseAddStudent, CourseAddSubject, CourseDetailsData, CourseDetailsStudents, CourseDetailsSubjects } from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { IconAlertCircle, IconArrowBack, IconArrowBigUpLines, IconBook, IconSchool } from '@tabler/icons';
import { Link, useParams } from 'react-router-dom';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';
import { CourseDetailsSection } from '../CourseDetailsSection';

export const CourseDetails = (): JSX.Element => {
  const { id } = useParams();
  const { t } = useTranslation();

  const {
    data: course,
    loading,
    error,
    reload,
  } = useFetch<CourseResponse>(`/api/course/${id}`);

  const [addStudentModalOpened, addStudentModalHandlers] = useDisclosure(false);
  const [addSubjectModalOpened, addSubjectModalHandlers] = useDisclosure(false);

  useDocumentTitle(course && `${course.code.number}${course.code.letter} (${course.schoolYear})`);

  const getErrorMessage = (error: AxiosError): string => {
    if (error.response?.status === 404) {
      return t('courses.courseDetails.notFound');
    }
    return t('courses.courseDetails.connectionFailed');
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
        title={t('courses.courseDetails.addStudentModalTitle')}
      >
        <CourseAddStudent
          course={course}
          onStudentRegistered={() => {
            addStudentModalHandlers.close();
            reload();
          }}
        />
      </Modal>}

      {course && <Modal
        onClose={addSubjectModalHandlers.close}
        opened={addSubjectModalOpened}
        size={'xl'}
        title={t('courses.courseDetails.addSubjectModalTitle')}
      >
        <CourseAddSubject
          courseId={course.id}
          onCourseCreated={() => {
            addSubjectModalHandlers.close();
            reload();
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
                leftIcon={<IconArrowBigUpLines size={16}/>}
                // onClick={editModalHandlers.open}
                variant={'default'}
                disabled={true} // TODO
              >
                {t('courses.courseDetails.promoteClass')}
              </Button>
            </div>
          </div>

          <div className={'course-details-head'}>
            <div className={'course-school-year'}>
              {course.schoolYear}
            </div>
            <div className={'course-code'}>
              {course.code.number}{course.code.letter}
            </div>
          </div>

          <div className={'course-details-meta'}>
            <CourseDetailsData course={course}/>
          </div>

          <CourseDetailsSection
            title={t('courses.courseDetails.students')}
            subtitle={course.students.length}
            actionButton={
              <Button
                fullWidth
                leftIcon={<IconSchool size={16}/>}
                onClick={addStudentModalHandlers.open}
                variant={'default'}
              >
                {t('courses.courseDetails.addStudent')}
              </Button>
            }
            content={
              <CourseDetailsStudents students={course.students}/>
            }
          />
          <CourseDetailsSection
            title={t('courses.courseDetails.subjects')}
            subtitle={course.subjects.length}
            actionButton={
              <Button
                fullWidth
                leftIcon={<IconBook size={16}/>}
                onClick={addSubjectModalHandlers.open}
                variant={'default'}
              >
                {t('courses.courseDetails.addSubject')}
              </Button>
            }
            content={
              <CourseDetailsSubjects subjects={course.subjects}/>
            }
          />
        </div>
      }
    </>
  );
};
