import './CourseEdit.scss';
import 'dayjs/locale/pl';
import { Button, Loader, NativeSelect, NumberInput, TextInput } from '@mantine/core';
import { CourseCreateRequest } from '../../../dto/course';
import { DatePicker } from '@mantine/dates';
import { TeachersResponse } from '../../../dto/teacher';
import { useFetch } from '../../../hooks/useFetch';
import { useForm } from '@mantine/form';

export const CourseEdit = (): JSX.Element => {
  const CODE_LETTERS_REGEXP = /^[a-z]+$/;
  const DATE_FORMAT = 'D MMMM YYYY';

  const { data, loading } = useFetch<TeachersResponse>('api/teacher');

  const form = useForm<CourseCreateRequest>({
    initialValues: {
      codeLetter: '',
      classTeacherId: '',
    },
    validate: (values) => ({
      codeNumber: !values.codeNumber ? 'Wymagane pole' : null,
      codeLetter: !values.codeLetter ? 'Wymagane pole' :
        !values.codeLetter?.match(CODE_LETTERS_REGEXP) ? 'Tylko małe litery bez polskich znaków' : null,
      schoolYearBegin: validateSchoolYearBegin(values),
      schoolYearSemesterEnd: validateSchoolYearSemesterEnd(values),
      schoolYearEnd: validateSchoolYearEnd(values),
      classTeacherId: !values.classTeacherId ? 'Wymagane pole' : null,
    }),
  });

  const validateSchoolYearBegin = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearBegin) {
      return 'Wymagane pole';
    }
    if (schoolYearSemesterEnd && schoolYearBegin >= schoolYearSemesterEnd) {
      return 'Rozpoczęcie roku musi następować przed końcem pierwszego semestru';
    }
    if (schoolYearEnd && schoolYearBegin >= schoolYearEnd) {
      return 'Rozpoczęcie roku musi następować przed końcem roku';
    }
    return null;
  };

  const validateSchoolYearSemesterEnd = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearSemesterEnd) {
      return 'Wymagane pole';
    }
    if (schoolYearBegin && schoolYearSemesterEnd <= schoolYearBegin) {
      return 'Koniec pierwszego semestru musi następować po rozpoczęciu roku';
    }
    if (schoolYearEnd && schoolYearSemesterEnd >= schoolYearEnd) {
      return 'Koniec pierwszego semestru musi następować przed końcem roku';
    }
    return null;
  };

  const validateSchoolYearEnd = (
    { schoolYearBegin, schoolYearSemesterEnd, schoolYearEnd }: CourseCreateRequest,
  ): string | null => {
    if (!schoolYearEnd) {
      return 'Wymagane pole';
    }
    if (schoolYearBegin && schoolYearEnd <= schoolYearBegin) {
      return 'Koniec roku musi następować po rozpoczęciu roku';
    }
    if (schoolYearSemesterEnd && schoolYearEnd <= schoolYearSemesterEnd) {
      return 'Koniec roku musi następować po końcu pierwszego semestru';
    }
    return null;
  };

  return (
    <div className={'course-edit'}>
      {loading && <Loader/>}
      {data && <form
        className={'course-edit-form'}
        onSubmit={form.onSubmit((formData) => console.log(formData))}
        noValidate
      >
        <div className={'course-edit-code'}>
          <NumberInput
            required
            style={{ width: '100%' }}
            label={'Numer klasy'}
            min={1}
            max={8}
            {...form.getInputProps('codeNumber')}/>
          <TextInput
            required
            style={{ width: '100%' }}
            label={'Oznaczenie literowe klasy'}
            {...form.getInputProps('codeLetter')}/>
        </div>

        <DatePicker
          required
          label={'Rozpoczęcie roku szkolnego'}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearBegin')}/>
        <DatePicker
          required
          label={'Koniec pierwszego semestru'}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearSemesterEnd')}/>
        <DatePicker
          required
          label={'Koniec roku szkolnego'}
          inputFormat={DATE_FORMAT}
          locale={'pl'}
          {...form.getInputProps('schoolYearEnd')}/>

        <NativeSelect
          required
          label={'Wychowawca'}
          placeholder={'Wybierz wychowawcę klasy'}
          data={
            data.teachers.map((teacher) =>
              ({ value: teacher.id, label: `${teacher.user.lastName} ${teacher.user.firstName} (${teacher.user.username})` }),
            )
          }
          {...form.getInputProps('classTeacherId')}/>

        <TextInput
          label={'Profil'}
          placeholder={'np. „matematyczno-fizyczny”'}
          {...form.getInputProps('profile')}/>
        <TextInput
          label={'Zawód'}
          placeholder={'np. „technik informatyk”'}
          {...form.getInputProps('profession')}/>
        <TextInput
          label={'Specjalność'}
          placeholder={'np. „zarządzanie projektami w IT”'}
          {...form.getInputProps('specialization')}/>

        <Button type={'submit'}>
          Utwórz
        </Button>
      </form>}
    </div>
  );
};
