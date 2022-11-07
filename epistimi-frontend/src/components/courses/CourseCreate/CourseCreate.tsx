import './CourseCreate.scss';
import 'dayjs/locale/pl';
import { Alert, Button, Loader, NativeSelect, NumberInput, TextInput } from '@mantine/core';
import { CourseCreateRequest, CourseResponse } from '../../../dto/course';
import { DatePicker } from '@mantine/dates';
import { IconAlertCircle } from '@tabler/icons';
import { TeachersResponse } from '../../../dto/teacher';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks';
import { useForm } from '@mantine/form';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

interface CourseCreateProps {
  submitCallback: (course: CourseResponse) => void;
}

export const CourseCreate = (props: CourseCreateProps): JSX.Element => {
  const CODE_LETTERS_REGEXP = /^[a-z]+$/;
  const DATE_FORMAT = 'D MMMM YYYY';

  const { data, loading } = useFetch<TeachersResponse>('api/teacher');
  const [errorMessageOpened, errorMessageHandlers] = useDisclosure(false);

  const { t } = useTranslation();

  const form = useForm<CourseCreateRequest>({
    initialValues: {
      codeLetter: '',
      classTeacherId: '',
    },
    validate: (values) => ({
      codeNumber: !values.codeNumber ? t('courses.courseEdit.requiredField') : null,
      codeLetter: !values.codeLetter ? t('courses.courseEdit.requiredField') :
        !values.codeLetter?.match(CODE_LETTERS_REGEXP) ? t('courses.courseEdit.codeLetterRegexpFail') : null,
      schoolYearBegin: validateSchoolYearBegin(values),
      schoolYearSemesterEnd: validateSchoolYearSemesterEnd(values),
      schoolYearEnd: validateSchoolYearEnd(values),
      classTeacherId: !values.classTeacherId ? t('courses.courseEdit.requiredField') : null,
    }),
  });

  const validateSchoolYearBegin = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearBegin) {
      return t('courses.courseEdit.requiredField');
    }
    if (schoolYearBegin <= new Date()) {
      return t('courses.courseEdit.schoolYearBeginningShouldBeInFuture');
    }
    if (schoolYearSemesterEnd && schoolYearBegin >= schoolYearSemesterEnd) {
      return t('courses.courseEdit.schoolYearBeginningShouldBeBeforeSemesterEnd');
    }
    if (schoolYearEnd && schoolYearBegin >= schoolYearEnd) {
      return t('courses.courseEdit.schoolYearBeginningShouldBeBeforeSchoolYearEnd');
    }
    return null;
  };

  const validateSchoolYearSemesterEnd = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearSemesterEnd) {
      return t('courses.courseEdit.requiredField');
    }
    if (schoolYearSemesterEnd <= new Date()) {
      return t('courses.courseEdit.schoolYearSemesterEndShouldBeInFuture');
    }
    if (schoolYearBegin && schoolYearSemesterEnd <= schoolYearBegin) {
      return t('courses.courseEdit.schoolYearSemesterEndShouldBeAfterSchoolYearBeginning');
    }
    if (schoolYearEnd && schoolYearSemesterEnd >= schoolYearEnd) {
      return t('courses.courseEdit.schoolYearSemesterEndShouldBeBeforeSchoolYearEnd');
    }
    return null;
  };

  const validateSchoolYearEnd = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearEnd) {
      return t('courses.courseEdit.requiredField');
    }
    if (schoolYearEnd <= new Date()) {
      return t('courses.courseEdit.schoolYearEndShouldBeInFuture');
    }
    if (schoolYearBegin && schoolYearEnd <= schoolYearBegin) {
      return t('courses.courseEdit.schoolYearEndShouldBeAfterSchoolYearBeginning');
    }
    if (schoolYearSemesterEnd && schoolYearEnd <= schoolYearSemesterEnd) {
      return t('courses.courseEdit.schoolYearEndShouldBeAfterSemesterEnd');
    }
    if (schoolYearBegin && schoolYearBegin.getFullYear() != (schoolYearEnd.getFullYear() - 1)) {
      return t('courses.courseEdit.schoolYearEndShouldBeNextCalendarYearAfterSchoolYearBeginning');
    }
    return null;
  };

  const submitHandler = (formData: CourseCreateRequest): void => {
    axios.post<CourseResponse, AxiosResponse<CourseResponse>, CourseCreateRequest>(
      '/api/course', formData,
    ).then((response) => {
      errorMessageHandlers.close();
      props.submitCallback(response.data);
    }).catch(() => {
      errorMessageHandlers.open();
    });
  };

  return (
    <div className={'course-edit'}>
      {loading && <Loader/>}
      {errorMessageOpened &&
        <Alert icon={<IconAlertCircle size={16}/>} title={t('courses.courseEdit.error')} color={'red'}>
          {t('courses.courseEdit.couldNotCreateCourse')}
        </Alert>
      }
      {data && <form
        className={'course-edit-form'}
        onSubmit={form.onSubmit(submitHandler)}
        noValidate
      >
        <div className={'course-edit-code'}>
          <NumberInput
            required
            style={{ width: '100%' }}
            label={t('courses.courseEdit.codeNumber')}
            min={1}
            max={8}
            {...form.getInputProps('codeNumber')}/>
          <TextInput
            required
            style={{ width: '100%' }}
            label={t('courses.courseEdit.codeLetter')}
            {...form.getInputProps('codeLetter')}/>
        </div>

        <DatePicker
          required
          label={t('courses.courseEdit.schoolYearBeginning')}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearBegin')}/>
        <DatePicker
          required
          label={t('courses.courseEdit.schoolYearSemesterEnd')}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearSemesterEnd')}/>
        <DatePicker
          required
          label={t('courses.courseEdit.schoolYearEnd')}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearEnd')}/>

        <NativeSelect
          required
          label={t('courses.courseEdit.classTeacher')}
          placeholder={t('courses.courseEdit.selectClassTeacher')}
          data={
            data.teachers.map((teacher) =>
              ({ value: teacher.id, label: `${teacher.user.lastName} ${teacher.user.firstName} (${teacher.user.username})` }),
            )
          }
          {...form.getInputProps('classTeacherId')}/>

        <TextInput
          label={t('courses.courseEdit.profile')}
          placeholder={t('courses.courseEdit.profilePlaceholder')}
          {...form.getInputProps('profile')}/>
        <TextInput
          label={t('courses.courseEdit.profession')}
          placeholder={t('courses.courseEdit.professionPlaceholder')}
          {...form.getInputProps('profession')}/>
        <TextInput
          label={t('courses.courseEdit.specialization')}
          placeholder={t('courses.courseEdit.specializationPlaceholder')}
          {...form.getInputProps('specialization')}/>

        <Button type={'submit'}>
          {t('courses.courseEdit.create')}
        </Button>
      </form>}
    </div>
  );
};
