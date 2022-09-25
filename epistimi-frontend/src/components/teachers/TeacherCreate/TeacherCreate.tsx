import './TeacherCreate.scss';
import { Address } from '../../../dto/address';
import { Button } from '@mantine/core';
import { IconCheck } from '@tabler/icons';
import { TeacherCreateStepper, TeacherCreateSummary, TeacherCreateUserForm } from '../../teachers';
import { TeacherRegisterRequest, TeacherRegisterResponse } from '../../../dto/teacher';
import { useForm } from '@mantine/form';
import { UserRole } from '../../../dto/user';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { validatePesel } from '../../../validators/pesel';
import axios from 'axios';

const enum TeacherCreateState {
  EDIT,
  SUMMARY,
}

export type TeacherRegisterFormData = {
  academicTitle: string;
  firstName: string;
  lastName: string;
  pesel: string;
  email: string;
  phoneNumber: string;
} & Address;

interface TeacherCreateProps {
  onTeacherRegistered: (response: TeacherRegisterResponse) => void,
}

export const TeacherCreate = (
  { onTeacherRegistered }: TeacherCreateProps,
): JSX.Element => {

  const { t } = useTranslation();

  const form = useForm<TeacherRegisterFormData>({
    initialValues: {
      academicTitle: '',
      firstName: '',
      lastName: '',
      pesel: '',
      email: '',
      phoneNumber: '',
      street: '',
      postalCode: '',
      city: '',
    },
    validate: (values) => ({
      firstName: !values.firstName ? t('teachers.teacherCreate.requiredField') : null,
      lastName: !values.lastName ? t('teachers.teacherCreate.requiredField') : null,
      pesel: !values.pesel ? t('teachers.teacherCreate.requiredField')
        : !validatePesel(values.pesel) ? t('teachers.teacherCreate.invalidPesel') : null,
    }),
  });

  const [modalState, setModalState] = useState<TeacherCreateState>(TeacherCreateState.EDIT);
  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [registerResponse, setRegisterResponse] = useState<TeacherRegisterResponse>();

  const sendRegisterRequest = () => {
    if (form.validate().hasErrors) return;
    setSendingRequest(true);

    const registerRequest: TeacherRegisterRequest = {
      user: {
        firstName: form.values.firstName,
        lastName: form.values.lastName,
        role: UserRole.TEACHER,
        pesel: form.values.pesel,
        email: form.values.email,
        phoneNumber: form.values.phoneNumber,
        address: {
          street: form.values.street,
          postalCode: form.values.postalCode,
          city: form.values.city,
        },
      },
      academicTitle: form.values.academicTitle,
    };

    axios.post('/api/teacher', registerRequest).then((response) => {
      setModalState(TeacherCreateState.SUMMARY);
      setSendingRequest(false);
      setRegisterResponse(response.data);
      onTeacherRegistered(response.data);
    }).catch(() => {
      setSendingRequest(false);
      // TODO: handle failures!
    });
  };

  return (
    <div className={'teacher-create'}>
      <TeacherCreateStepper step={modalState.valueOf()}/>

      {modalState === TeacherCreateState.EDIT && <>
        <TeacherCreateUserForm form={form}/>
        <Button
          leftIcon={<IconCheck size={18}/>}
          onClick={sendRegisterRequest}
          loading={sendingRequest}
        >
          {t('teachers.teacherCreate.addTeacher')}
        </Button>
      </>}

      {modalState === TeacherCreateState.SUMMARY &&
        <TeacherCreateSummary registerResponse={registerResponse}/>}
    </div>
  );
};
