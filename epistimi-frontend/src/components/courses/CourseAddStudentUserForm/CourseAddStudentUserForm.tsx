import './CourseAddStudentUserForm.scss';
import { TextInput } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form/lib/use-form';
import { UserFormData } from '../CourseAddStudent';
import { useTranslation } from 'react-i18next';

interface CourseAddStudentUserFormProps {
  formData: UseFormReturnType<UserFormData>;
  disabled: boolean;
}

export const CourseAddStudentUserForm = (
  { formData, disabled }: CourseAddStudentUserFormProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <form className={'add-student-form'}>
      <TextInput
        label={t('courses.courseAddStudentUserForm.firstName')}
        required={true}
        disabled={disabled}
        {...formData.getInputProps('firstName')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.lastName')}
        required={true}
        disabled={disabled}
        {...formData.getInputProps('lastName')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.pesel')}
        required={true}
        disabled={disabled}
        {...formData.getInputProps('pesel')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.email')}
        disabled={disabled}
        {...formData.getInputProps('email')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.phoneNumber')}
        disabled={disabled}
        {...formData.getInputProps('phoneNumber')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.street')}
        disabled={disabled}
        {...formData.getInputProps('street')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.postalCode')}
        disabled={disabled}
        {...formData.getInputProps('postalCode')}/>
      <TextInput
        label={t('courses.courseAddStudentUserForm.city')}
        disabled={disabled}
        {...formData.getInputProps('city')}/>
    </form>
  );
};
