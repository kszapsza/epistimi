import './CourseAddStudent.scss';
import { Address } from '../../../dto/address';
import { Button, Stepper, TextInput } from '@mantine/core';
import { CourseResponse } from '../../../dto/course';
import { IconArrowLeft, IconArrowRight, IconCheck, IconPlus, IconX } from '@tabler/icons';
import { StudentRegisterRequest, StudentRegisterResponse } from '../../../dto/student';
import { useForm } from '@mantine/form';
import { UserRegisterRequest, UserRole, UserSex } from '../../../dto/user';
import { useState } from 'react';
import { validatePesel } from '../../../validators/pesel';
import axios, { AxiosResponse } from 'axios';

interface CourseAddStudentProps {
  course: CourseResponse;
}

enum CourseAddStudentState {
  EDIT_STUDENT,
  EDIT_PARENTS,
  SUMMARY,
}

type UserRegisterFormData = {
  firstName: string;
  lastName: string;
  pesel?: string;
  sex?: UserSex;
  email?: string;
  phoneNumber?: string;
} & Address;

export const CourseAddStudent = (
  { course: { id: courseId } }: CourseAddStudentProps,
): JSX.Element => {

  const userInitialValues = {
    firstName: '',
    lastName: '',
    pesel: '',
    email: '',
    phoneNumber: '',
    street: '',
    postalCode: '',
    city: '',
    countryCode: 'PL',
  };

  const [modalState, setModalState] = useState<CourseAddStudentState>(CourseAddStudentState.EDIT_STUDENT);
  const [studentFormData, setStudentFormData] = useState<UserRegisterFormData>(userInitialValues);
  const [parentFormData, setParentFormData] = useState<UserRegisterFormData>(userInitialValues);
  const [parentList, setParentList] = useState<UserRegisterFormData[]>([]);

  const form = useForm<UserRegisterFormData>({
    initialValues: {
      ...userInitialValues,
    },
    validate: (values) => ({
      firstName: !values.firstName ? 'Wymagane pole' : null,
      lastName: !values.lastName ? 'Wymagane pole' : null,
      pesel: !values.pesel ? 'Wymagane pole'
        : !validatePesel(values.pesel) ? 'Niepoprawny PESEL' : null,
    }),
  });

  const openEditParentsView = (): void => {
    if (form.validate().hasErrors) return;

    setModalState(CourseAddStudentState.EDIT_PARENTS);
    setStudentFormData(form.values);

    form.clearErrors();
    form.setValues(parentFormData);
  };

  const openEditStudentView = (): void => {
    setModalState(CourseAddStudentState.EDIT_STUDENT);
    setParentFormData(form.values);

    form.clearErrors();
    form.setValues(studentFormData);
  };

  const addToParentsList = (): void => {
    if (form.validate().hasErrors) return;

    setModalState(CourseAddStudentState.EDIT_PARENTS);
    setParentList([...parentList, form.values]);

    form.clearErrors();
    form.setValues(userInitialValues);
  };

  const removeFromParentsList = (idx: number): void => {
    const newParentList = [...parentList];
    newParentList.splice(idx, 1);
    setParentList(newParentList);
  };

  const isUserFormDisabled = (): boolean => {
    return modalState === CourseAddStudentState.EDIT_PARENTS && parentList.length === 2;
  };

  const sendRegisterRequest = (): void => {
    const registerRequest: StudentRegisterRequest = {
      courseId,
      user: buildUserRegisterRequest(studentFormData, UserRole.STUDENT),
      parents: parentList.map((parentFormData) =>
        buildUserRegisterRequest(parentFormData, UserRole.PARENT)),
    };

    axios.post<StudentRegisterResponse, AxiosResponse<StudentRegisterResponse>, StudentRegisterRequest>(
      '/api/student', registerRequest,
    ).then((response) => {
      // errorMessageHandlers.close();
      setModalState(CourseAddStudentState.SUMMARY);
    }).catch(() => {
      // errorMessageHandlers.open();
    });
  };

  const buildUserRegisterRequest = (
    formData: UserRegisterFormData,
    role: UserRole,
  ): UserRegisterRequest => {
    return {
      firstName: formData.firstName,
      lastName: formData.lastName,
      role,
      pesel: formData.pesel,
      sex: formData.sex,
      email: formData.email,
      phoneNumber: formData.phoneNumber,
      address: {
        street: formData.street,
        postalCode: formData.postalCode,
        city: formData.city,
        countryCode: formData.countryCode,
      },
    };
  };

  return (
    <div className={'add-student'}>
      <Stepper active={modalState.valueOf()} breakpoint={'sm'} className={'add-student-stepper'}>
        <Stepper.Step label={'Uczeń'} description={'Edytuj dane ucznia'}/>
        <Stepper.Step label={'Rodzice'} description={'Edytuj dane rodziców'}/>
        <Stepper.Step label={'Podsumowanie'} description={'Wygenerowane loginy i hasła'}/>
      </Stepper>

      {modalState === CourseAddStudentState.EDIT_PARENTS && parentList.length > 0 && (
        <div className={'add-student-parent-list'}>
          {parentList.map((parent, idx) => (
            <div className={'add-student-parent-entry'} key={idx}>
              <div className={'add-student-parent-name'}>
                {parent.firstName} {parent.lastName}
              </div>
              <div className={'add-student-parent-actions'}>
                <IconX size={18} onClick={() => removeFromParentsList(idx)}/>
              </div>
            </div>
          ))}
        </div>
      )}

      <form className={'add-student-form'}>
        <TextInput
          label={'Imię'}
          required={true}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('firstName')}/>
        <TextInput
          label={'Nazwisko'}
          required={true}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('lastName')}/>
        <TextInput
          label={'PESEL'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('pesel')}/>
        <TextInput
          label={'E-mail'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('email')}/>
        <TextInput
          label={'Numer telefonu'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('phoneNumber')}/>
        <TextInput
          label={'Ulica'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('street')}/>
        <TextInput
          label={'Kod pocztowy'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('postalCode')}/>
        <TextInput
          label={'Miasto'}
          disabled={isUserFormDisabled()}
          {...form.getInputProps('city')}/>
      </form>

      <div className={'add-student-actions'}>
        {modalState === CourseAddStudentState.EDIT_STUDENT && (
          <Button
            rightIcon={<IconArrowRight size={18}/>}
            variant={'outline'}
            onClick={openEditParentsView}
          >
            Dane rodziców
          </Button>)}
        {modalState === CourseAddStudentState.EDIT_PARENTS && (
          <Button
            leftIcon={<IconArrowLeft size={18}/>}
            variant={'outline'}
            onClick={openEditStudentView}
          >
            Dane ucznia
          </Button>)}
        {modalState === CourseAddStudentState.EDIT_PARENTS && parentList.length < 2 && (
          <Button
            leftIcon={<IconPlus size={18}/>}
            variant={'outline'}
            onClick={addToParentsList}
          >
            Dodaj rodzica
          </Button>)}
        {modalState === CourseAddStudentState.EDIT_PARENTS && parentList.length >= 1 && (
          <Button
            leftIcon={<IconCheck size={18}/>}
            onClick={sendRegisterRequest}
          >
            Dodaj ucznia do klasy
          </Button>)}
      </div>
    </div>
  );
};
