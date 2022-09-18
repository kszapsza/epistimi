import { TeacherRegisterFormData } from '../TeacherCreate';
import { TextInput } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form/lib/use-form';

interface TeacherCreateUserFormProps {
  form: UseFormReturnType<TeacherRegisterFormData>;
}

export const TeacherCreateUserForm = (
  { form }: TeacherCreateUserFormProps,
): JSX.Element => {
  return (
    <form className={'add-student-form'}>
      <TextInput
        label={'Tytuł naukowy'}
        {...form.getInputProps('academicTitle')}/>
      <TextInput
        label={'Imię'}
        required={true}
        {...form.getInputProps('firstName')}/>
      <TextInput
        label={'Nazwisko'}
        required={true}
        {...form.getInputProps('lastName')}/>
      <TextInput
        label={'PESEL'}
        required={true}
        {...form.getInputProps('pesel')}/>
      <TextInput
        label={'E-mail'}
        {...form.getInputProps('email')}/>
      <TextInput
        label={'Numer telefonu'}
        {...form.getInputProps('phoneNumber')}/>
      <TextInput
        label={'Ulica'}
        {...form.getInputProps('street')}/>
      <TextInput
        label={'Kod pocztowy'}
        {...form.getInputProps('postalCode')}/>
      <TextInput
        label={'Miasto'}
        {...form.getInputProps('city')}/>
    </form>
  );
};
