import './SubjectGradesTeacherIssueClassificationGradeForm.scss';
import { Alert, Button, Checkbox, LoadingOverlay, Radio, Select, TextInput } from '@mantine/core';
import { CLASSIFICATION_GRADE_SELECT_VALUES } from '../../../dto/grade-values';
import { ClassificationGradeIssueRequest, ClassificationGradePeriod, ClassificationGradeResponse } from '../../../dto/classification-grade';
import { IconAlertCircle, IconStar, IconStarHalf } from '@tabler/icons';
import { SubjectGradesStudentResponse } from '../../../dto/subject-grades';
import { SubjectResponse } from '../../../dto/subject';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface SubjectGradesTeacherIssueClassificationGradeFormProps {
  subject: SubjectResponse;
  student: SubjectGradesStudentResponse;
  semester: number;
  onNewClassificationGradeIssued: (response: ClassificationGradeResponse) => void;
}

export const SubjectGradesTeacherIssueClassificationGradeForm = (
  { subject, student, semester, onNewClassificationGradeIssued }: SubjectGradesTeacherIssueClassificationGradeFormProps,
): JSX.Element => {

  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [error, setError] = useState<boolean>(false);

  const form = useForm<Partial<ClassificationGradeIssueRequest>>({
    initialValues: {
      subjectId: subject.id,
      studentId: student.id,
      period: (semester === 1)
        ? ClassificationGradePeriod.FIRST_SEMESTER
        : ClassificationGradePeriod.SECOND_SEMESTER,
      isProposal: true,
    },
    validate: (values) => ({
      value: !values.value ? 'Wartość jest wymagana' : null,
    }),
  });

  const getGradeValueInputDescription = (): string | null => {
    switch (form.values.period) {
      case ClassificationGradePeriod.FIRST_SEMESTER:
        return `Średnia ucznia za semestr I: ${student.firstSemester.average ?? '–'}`;
      case ClassificationGradePeriod.SECOND_SEMESTER:
        return `Średnia ucznia za semestr II: ${student.secondSemester.average ?? '–'}`;
      case ClassificationGradePeriod.SCHOOL_YEAR:
        return `Średnia roczna ucznia: ${student.average ?? '–'}`;
      default:
        return null;
    }
  };

  const handleSubmit = () => {
    setSendingRequest(true);
    axios.post<ClassificationGradeResponse, AxiosResponse<ClassificationGradeResponse>, ClassificationGradeIssueRequest>(
      '/api/grade/classification', form.values as ClassificationGradeIssueRequest)
      .then((response) => {
        setSendingRequest(false);
        onNewClassificationGradeIssued(response.data);
      })
      .catch(() => {
        setSendingRequest(false);
        setError(true);
      });
  };

  return (
    <>
      <LoadingOverlay visible={sendingRequest}/>
      <form
        className={'subject-grades-teacher-issue-classification-form'}
        onSubmit={form.onSubmit(handleSubmit)}
        noValidate
      >
        {!form.values.isProposal && (
          <Alert icon={<IconAlertCircle size={16}/>} color={'orange'} title={'Wystawianie oceny klasyfikacyjnej!'}>
            Poprawienie ostatecznej oceny klasyfikacyjnej nie będzie możliwe.
          </Alert>
        )}
        {error && (
          <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={'Wystawianie oceny klasyfikacyjnej!'}>
            Wystąpił błąd podczas wystawiania oceny klasyfikacyjnej
          </Alert>
        )}

        <div className={'subject-grades-teacher-issue-classification-form-group'}>
          <TextInput
            readOnly
            label={'Przedmiot'}
            value={`${subject.name}`}
            style={{ flex: 1 }}
          />
          <TextInput
            readOnly
            label={'Klasa'}
            value={`${subject.course.code} (${subject.course.schoolYear})`}
            style={{ flex: 1 }}
          />
        </div>

        <TextInput
          readOnly
          label={'Uczeń'}
          value={`${student.lastName} ${student.firstName}`}
        />

        <Radio.Group
          name={'period'}
          label={'Oceniany okres'}
          withAsterisk
          {...form.getInputProps('period')}
        >
          <Radio value={ClassificationGradePeriod.FIRST_SEMESTER} label={'Semestr I'}/>
          <Radio value={ClassificationGradePeriod.SECOND_SEMESTER} label={'Semestr II'}/>
          <Radio value={ClassificationGradePeriod.SCHOOL_YEAR} label={'Rok szkolny'}/>
        </Radio.Group>

        <Select
          required
          searchable
          clearable
          label={'Ocena'}
          description={getGradeValueInputDescription()}
          placeholder={'Wyszukaj i wybierz wartość oceny'}
          data={CLASSIFICATION_GRADE_SELECT_VALUES}
          style={{ flex: 3 }}
          {...form.getInputProps('value')}
        />

        <Checkbox
          label={'Wystaw jako propozycję oceny'}
          checked={form.values.isProposal}
          onChange={(e) => form.setFieldValue('isProposal', e.currentTarget.checked)}
          {...form.getInputProps('isProposal')}
        />

        <Button
          leftIcon={form.values.isProposal
            ? <IconStarHalf size={18}/>
            : <IconStar size={18}/>}
          type={'submit'}
        >
          {form.values.isProposal
            ? 'Wystaw propozycję oceny'
            : 'Wystaw ocenę klasyfikacyjną'}
        </Button>
      </form>
    </>
  );
};

