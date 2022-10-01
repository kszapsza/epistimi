import { IconAt, IconPhone } from '@tabler/icons';
import { TeacherRegisterFormData } from '../TeacherCreate';
import { TextInput } from '@mantine/core';
import { UseFormReturnType } from '@mantine/form';
import { useTranslation } from 'react-i18next';

interface TeacherCreateUserFormProps {
  form: UseFormReturnType<TeacherRegisterFormData>;
}

export const TeacherCreateUserForm = (
  { form }: TeacherCreateUserFormProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <form className={'add-student-form'}>
      <TextInput
        label={t('teachers.teacherCreateUserForm.academicTitle')}
        {...form.getInputProps('academicTitle')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.firstName')}
        required={true}
        {...form.getInputProps('firstName')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.lastName')}
        required={true}
        {...form.getInputProps('lastName')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.pesel')}
        required={true}
        {...form.getInputProps('pesel')}/>
      <TextInput
        icon={<IconAt size={16} />}
        label={t('teachers.teacherCreateUserForm.email')}
        {...form.getInputProps('email')}/>
      <TextInput
        icon={<IconPhone size={16} />}
        label={t('teachers.teacherCreateUserForm.phoneNumber')}
        {...form.getInputProps('phoneNumber')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.street')}
        {...form.getInputProps('street')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.postalCode')}
        {...form.getInputProps('postalCode')}/>
      <TextInput
        label={t('teachers.teacherCreateUserForm.city')}
        {...form.getInputProps('city')}/>
    </form>
  );
};
