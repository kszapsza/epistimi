import './CourseEdit.scss';
import 'dayjs/locale/pl';
import { Button, NativeSelect, NumberInput, TextInput } from '@mantine/core';
import { CourseCreateRequest } from '../../../dto/course';
import { DatePicker } from '@mantine/dates';
import { useForm } from '@mantine/form';

export const CourseEdit = (): JSX.Element => {
  const CODE_LETTERS_REGEXP = /^[a-z]+$/;
  const DATE_FORMAT = 'D MMMM YYYY';

  const form = useForm<CourseCreateRequest>({
    initialValues: {
      codeLetter: '',
      classTeacherId: '',
    },
    validate: (values) => ({ // TODO: walidacja kolejności dat + czy nie występują w przeszłości
      codeNumber: !values.codeNumber ? 'Wymagane pole' : null,
      codeLetter: !values.codeLetter ? 'Wymagane pole' :
        !values.codeLetter?.match(CODE_LETTERS_REGEXP) ? 'Tylko małe litery bez polskich znaków' : null,
      schoolYearBegin: !values.schoolYearBegin ? 'Wymagane pole' : null,
      schoolYearSemesterEnd: !values.schoolYearSemesterEnd ? 'Wymagane pole' : null,
      schoolYearEnd: !values.schoolYearEnd ? 'Wymagane pole' : null,
      classTeacherId: !values.classTeacherId ? 'Wymagane pole' : null,
    }),
  });

  return (
    <div className={'course-edit'}>
      <form
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
          data={[]}
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
      </form>
    </div>
  );
};
