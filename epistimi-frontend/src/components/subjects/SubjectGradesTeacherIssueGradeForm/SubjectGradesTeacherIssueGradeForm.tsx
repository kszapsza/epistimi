import './SubjectGradesTeacherIssueGradeForm.scss';
import {
  Alert,
  Button,
  Checkbox,
  LoadingOverlay,
  NumberInput,
  Select,
  Textarea,
  TextInput,
} from '@mantine/core';
import { GRADE_SELECT_VALUES } from '../../../dto/grade-values';
import { GradeCategoriesResponse } from '../../../dto/grade-category';
import { GradeIssueRequest, GradeResponse } from '../../../dto/grade';
import { IconAlertCircle, IconCheck } from '@tabler/icons';
import { SubjectGradesStudentResponse } from '../../../dto/subject-grades';
import { SubjectResponse } from '../../../dto/subject';
import { useFetch } from '../../../hooks';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface SubjectGradesTeacherIssueGradeFormProps {
  subject: SubjectResponse;
  student: SubjectGradesStudentResponse;
  semester: number;
  onNewGradeIssued: (response: GradeResponse) => void;
}

export const SubjectGradesTeacherIssueGradeForm = (
  { subject, student, semester, onNewGradeIssued }: SubjectGradesTeacherIssueGradeFormProps,
): JSX.Element => {

  const {
    data: categoriesData,
    loading: categoriesLoading,
  } = useFetch<GradeCategoriesResponse>(`/api/grade/category?subjectId=${subject.id}`);

  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [submitError, setSubmitError] = useState<boolean>(false);

  const form = useForm<Partial<GradeIssueRequest>>({
    initialValues: {
      subjectId: subject.id,
      studentId: student.id,
      semester: semester,
      countTowardsAverage: true,
    },
    validate: (values) => ({
      categoryId: !values.categoryId ? 'Kategoria jest wymagana' : null,
      value: !values.value ? 'Wartość jest wymagana' : null,
      weight: !values.weight ? 'Waga jest wymagana' : null,
    }),
  });

  const onCategoryChange = (categoryId: string | null): void => {
    if (!categoryId) {
      return;
    }
    const defaultCategoryWeight = categoriesData?.categories
      .find((category) => category.id === categoryId)?.defaultWeight ?? 10;
    form.setFieldValue('categoryId', categoryId);
    form.setFieldValue('weight', defaultCategoryWeight);
  };

  const handleSubmit = (): void => {
    axios.post<GradeResponse, AxiosResponse<GradeResponse>, GradeIssueRequest>(
      '/api/grade', form.values as GradeIssueRequest) // form is validated, cast is safe
      .then((response) => {
        setSendingRequest(false);
        onNewGradeIssued(response.data);
      })
      .catch(() => {
        setSendingRequest(false);
        setSubmitError(true);
      });
  };

  return (
    <>
      <LoadingOverlay visible={categoriesLoading || sendingRequest}/>
      {categoriesData && categoriesData.categories.length === 0 &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'orange'} title={'Nie zdefiniowano kategorii ocen'}>
          Przed rozpoczęciem operacji zdefiniuj kategorie ocen w zakładce <em>Konfiguracja</em>.
        </Alert>}

      {categoriesData && categoriesData.categories.length !== 0 && (
        <form
          noValidate
          onSubmit={form.onSubmit(handleSubmit)}
          className={'subject-grades-teacher-issue-form'}
        >
          {submitError &&
            <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={'Błąd'}>
              Wystawianie oceny nie powiodło się
            </Alert>}

          <div className={'subject-grades-teacher-issue-form-group'}>
            <TextInput
              readOnly
              label={'Przedmiot'}
              value={`${subject.name}`}
              style={{ flex: 2 }}
            />
            <TextInput
              readOnly
              label={'Klasa'}
              value={`${subject.course.code} (${subject.course.schoolYear})`}
              style={{ flex: 1 }}
            />
            <TextInput
              readOnly
              label={'Semestr'}
              value={`${semester}`}
              style={{ flex: 1 }}
            />
          </div>

          <TextInput
            readOnly
            label={'Uczeń'}
            value={`${student.lastName} ${student.firstName}`}
          />

          <Select
            required
            searchable
            label={'Kategoria'}
            placeholder={'Wyszukaj i wybierz kategorię oceny'}
            data={categoriesData ? categoriesData.categories.map((category) => (
              { value: category.id, label: category.name }
            )) : []}
            style={{ flex: 3 }}
            {...form.getInputProps('categoryId')}
            onChange={onCategoryChange} // override
          />

          <div className={'subject-grades-teacher-issue-form-group'}>
            <Select
              required
              searchable
              clearable
              label={'Ocena'}
              placeholder={'Wyszukaj i wybierz wartość oceny'}
              data={GRADE_SELECT_VALUES}
              style={{ flex: 3 }}
              {...form.getInputProps('value')}
            />
            <NumberInput
              required
              min={1}
              max={10}
              label={'Waga'}
              style={{ flex: 1 }}
              {...form.getInputProps('weight')}
            />
          </div>

          <Checkbox
            label={'Licz do średniej'}
            checked={form.values.countTowardsAverage}
            onChange={(e) => form.setFieldValue('countTowardsAverage', e.currentTarget.checked)}
            {...form.getInputProps('countTowardsAverage')}
          />

          <Textarea
            label={'Komentarz'}
            placeholder={'Komentarz do oceny'}
            {...form.getInputProps('comment')}
          />

          <Button
            leftIcon={<IconCheck size={18}/>}
            type={'submit'}
          >
            Wystaw ocenę
          </Button>
        </form>)}
    </>
  );
};

