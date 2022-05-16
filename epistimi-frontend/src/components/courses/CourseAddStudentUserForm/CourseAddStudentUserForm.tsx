import './CourseAddStudentUserForm.scss';
import { TextInput } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form/lib/use-form';
import { UserRegisterFormData } from '../CourseAddStudent';

interface CourseAddStudentUserFormProps {
  formData: UseFormReturnType<UserRegisterFormData>;
  disabled: boolean;
}

export const CourseAddStudentUserForm = (
  { formData, disabled }: CourseAddStudentUserFormProps,
): JSX.Element => {
  return (
    <form className={'add-student-form'}>
      <TextInput
        label={'Imię'}
        required={true}
        disabled={disabled}
        {...formData.getInputProps('firstName')}/>
      <TextInput
        label={'Nazwisko'}
        required={true}
        disabled={disabled}
        {...formData.getInputProps('lastName')}/>
      <TextInput
        label={'PESEL'}
        disabled={disabled}
        {...formData.getInputProps('pesel')}/>
      <TextInput
        label={'E-mail'}
        disabled={disabled}
        {...formData.getInputProps('email')}/>
      <TextInput
        label={'Numer telefonu'}
        disabled={disabled}
        {...formData.getInputProps('phoneNumber')}/>
      <TextInput
        label={'Ulica'}
        disabled={disabled}
        {...formData.getInputProps('street')}/>
      <TextInput
        label={'Kod pocztowy'}
        disabled={disabled}
        {...formData.getInputProps('postalCode')}/>
      <TextInput
        label={'Miasto'}
        disabled={disabled}
        {...formData.getInputProps('city')}/>
    </form>
  );
};
