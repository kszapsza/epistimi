import './CourseAddSubject.scss';
import { Alert, Button, Loader, NativeSelect, TextInput } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { SubjectRegisterRequest, SubjectResponse } from '../../../dto/subject';
import { TeachersResponse } from '../../../dto/teacher';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks';
import { useForm } from '@mantine/form';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

interface CourseAddSubjectProps {
  courseId: string;
  onCourseCreated: (response: SubjectResponse) => void;
}

export const CourseAddSubject = ({ courseId, onCourseCreated }: CourseAddSubjectProps): JSX.Element => {
  const { data, loading } = useFetch<TeachersResponse>('api/teacher');
  const [errorMessageOpened, errorMessageHandlers] = useDisclosure(false);

  const { t } = useTranslation();

  const form = useForm<SubjectRegisterRequest>({
    initialValues: {
      courseId,
      name: '',
      teacherId: '',
    },
    validate: (values) => ({
      name: !values.name ? t('courses.courseAddSubject.requiredField') : null,
      teacherId: !values.teacherId ? t('courses.courseAddSubject.selectSubjectTeacher') : null,
    }),
  });

  const submitHandler = (formData: SubjectRegisterRequest): void => {
    axios.post<SubjectResponse, AxiosResponse<SubjectResponse>, SubjectRegisterRequest>(
      'api/subject', formData,
    ).then((response) => {
      errorMessageHandlers.close();
      onCourseCreated(response.data);
    }).catch(() => {
      errorMessageHandlers.open();
    });
  };

  return (
    <div className={'course-add-subject'}>
      {loading && <Loader/>}
      {errorMessageOpened &&
        <Alert icon={<IconAlertCircle size={16}/>} title={t('courses.courseEdit.error')} color={'red'}>
          {t('courses.courseAddSubject.couldNotCreateSubject')}
        </Alert>
      }
      {data && <form
        className={'course-add-subject-form'}
        onSubmit={form.onSubmit(submitHandler)}
        noValidate
      >
        <TextInput
          required
          label={t('courses.courseAddSubject.name')}
          placeholder={t('courses.courseAddSubject.namePlaceholder')}
          {...form.getInputProps('name')}
        />
        <NativeSelect
          required
          label={t('courses.courseAddSubject.teacher')}
          placeholder={t('courses.courseAddSubject.selectSubjectTeacher')}
          data={
            data.teachers.map((teacher) =>
              ({ value: teacher.id, label: `${teacher.user.lastName} ${teacher.user.firstName} (${teacher.user.username})` }),
            )
          }
          {...form.getInputProps('teacherId')}
        />
        <Button type={'submit'}>
          {t('courses.courseAddSubject.submit')}
        </Button>
      </form>}
    </div>
  );
};
