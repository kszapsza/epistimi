import './SubjectConfigurationCategoryForm.scss';
import {
  Alert,
  Button,
  Card,
  ColorPicker,
  DEFAULT_THEME,
  LoadingOverlay,
  NumberInput,
  TextInput,
  Title,
} from '@mantine/core';
import { GradeBadgeDropdown } from '../../grades';
import {
  GradeCategoriesResponseEntry,
  GradeCategoryCreateRequest,
  GradeCategoryResponse,
  GradeCategoryUpdateRequest,
} from '../../../dto/grade-category';
import { GradeResponse } from '../../../dto/grade';
import { IconAlertCircle, IconDeviceFloppy, IconPlus } from '@tabler/icons';
import { SubjectResponse } from '../../../dto/subject';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface SubjectConfigurationCategoryFormProps {
  subject: SubjectResponse;
  onSubmit: (newCategory: GradeCategoryResponse) => void;
  mode: SubjectConfigurationCategoryFormMode;
  existingCategory?: GradeCategoriesResponseEntry;
}

export const enum SubjectConfigurationCategoryFormMode {
  CREATE,
  UPDATE,
}

export const SubjectConfigurationCategoryForm = (
  props: SubjectConfigurationCategoryFormProps,
): JSX.Element => {

  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [submitError, setSubmitError] = useState<boolean>(false);

  const form = useForm<GradeCategoryCreateRequest>({
    initialValues: {
      subjectId: props.subject.id,
      name: props.existingCategory?.name ?? '',
      defaultWeight: props.existingCategory?.defaultWeight ?? 1,
      color: props.existingCategory?.color ?? DEFAULT_THEME.colors.blue[6],
    },
    validate: (values) => ({
      name: values.name.trim() === '' ? 'Wymagane pole' : null,
    }),
  });

  const onSubmit = (): void => {
    sendAxiosRequest()
      .then((response) => {
        setSendingRequest(false);
        props.onSubmit(response.data);
      })
      .catch(() => {
        setSendingRequest(false);
        setSubmitError(true);
      });
  };

  const sendAxiosRequest = async (): Promise<AxiosResponse<GradeCategoryResponse>> => {
    if (props.existingCategory &&
      props.mode === SubjectConfigurationCategoryFormMode.UPDATE) {
      return axios.put<
        GradeCategoryResponse,
        AxiosResponse<GradeCategoryResponse>,
        GradeCategoryUpdateRequest>(
        '/api/grade/category', { ...form.values, categoryId: props.existingCategory.id });
    } else {
      return axios.post<
        GradeCategoryResponse,
        AxiosResponse<GradeCategoryResponse>,
        GradeCategoryCreateRequest>(
        '/api/grade/category', form.values);
    }
  };

  const gradeSample: GradeResponse = {
    id: '',
    issuedBy: {
      id: '',
      firstName: props.subject.teacher.user.firstName,
      lastName: props.subject.teacher.user.lastName,
    },
    category: {
      id: '',
      name: (form.values.name) ? form.values.name : 'Nazwa kategorii',
      color: form.values.color,
    },
    semester: 1,
    issuedAt: new Date(2022, 2, 22, 22, 22, 22),
    updatedAt: new Date(2024, 4, 24, 23, 23, 23),
    value: {
      displayName: '3+',
      fullName: 'dostateczny plus',
    },
    weight: form.values.defaultWeight,
    countTowardsAverage: true,
    comment: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit',
  };

  return (
    <form
      noValidate
      className={'subject-configuration-create-category'}
      onSubmit={form.onSubmit(onSubmit)}
    >
      <LoadingOverlay visible={sendingRequest}/>

      {submitError &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={'Błąd'}>
          {props.mode === SubjectConfigurationCategoryFormMode.CREATE
            ? 'Nie udało się utworzyć nowej kategorii'
            : 'Nie udało się zaktualizować kategorii'}
        </Alert>}

      <TextInput
        autoFocus
        required
        label={'Nazwa kategorii'}
        {...form.getInputProps('name')}
      />
      <NumberInput
        required
        defaultValue={1}
        min={1}
        max={10}
        label={'Domyślna waga oceny'}
        {...form.getInputProps('defaultWeight')}
      />

      <Title order={5}>
        Wybór koloru
      </Title>
      <div className={'subject-configuration-create-category-color'}>
        <ColorPicker
          format={'hex'}
          withPicker
          size={'sm'}
          swatches={[
            ...DEFAULT_THEME.colors.red,
            ...DEFAULT_THEME.colors.pink,
            ...DEFAULT_THEME.colors.grape,
            ...DEFAULT_THEME.colors.blue,
            ...DEFAULT_THEME.colors.cyan,
            ...DEFAULT_THEME.colors.teal,
            ...DEFAULT_THEME.colors.green,
            ...DEFAULT_THEME.colors.yellow,
            ...DEFAULT_THEME.colors.orange,
          ]}
          value={form.values.color}
          onChange={(value) => form.setFieldValue('color', value)}
        />
        <Card style={{ width: '300px' }}>
          <GradeBadgeDropdown grade={gradeSample} showActions={false}/>
        </Card>
      </div>

      {props.mode === SubjectConfigurationCategoryFormMode.CREATE && (
        <Button
          leftIcon={<IconPlus size={18}/>}
          type={'submit'}
        >
          Utwórz kategorię
        </Button>)}
      {props.mode === SubjectConfigurationCategoryFormMode.UPDATE && (
        <Button
          leftIcon={<IconDeviceFloppy size={18}/>}
          type={'submit'}
        >
          Zaktualizuj kategorię
        </Button>)}
    </form>
  );
};

