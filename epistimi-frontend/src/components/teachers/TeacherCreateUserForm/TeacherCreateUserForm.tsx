import { TeacherRegisterFormData } from '../TeacherCreate/TeacherCreate';
import { TextInput } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form/lib/use-form';

interface TeacherCreateUserFormProps {
  formData: UseFormReturnType<TeacherRegisterFormData>;
}

export const TeacherCreateUserForm = (
  { formData }: TeacherCreateUserFormProps,
): JSX.Element => {
  return (
    <form className={'add-student-form'}>
      <TextInput
        label={'Tytuł naukowy'}
        {...formData.getInputProps('academicTitle')}/>
      <TextInput
        label={'Imię'}
        required={true}
        {...formData.getInputProps('firstName')}/>
      <TextInput
        label={'Nazwisko'}
        required={true}
        {...formData.getInputProps('lastName')}/>
      <TextInput
        label={'PESEL'}
        required={true}
        {...formData.getInputProps('pesel')}/>
      <TextInput
        label={'E-mail'}
        {...formData.getInputProps('email')}/>
      <TextInput
        label={'Numer telefonu'}
        {...formData.getInputProps('phoneNumber')}/>
      <TextInput
        label={'Ulica'}
        {...formData.getInputProps('street')}/>
      <TextInput
        label={'Kod pocztowy'}
        {...formData.getInputProps('postalCode')}/>
      <TextInput
        label={'Miasto'}
        {...formData.getInputProps('city')}/>
    </form>
  );
};
