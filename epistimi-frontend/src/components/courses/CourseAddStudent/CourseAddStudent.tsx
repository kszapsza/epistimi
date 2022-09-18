import './CourseAddStudent.scss';
import { Address } from '../../../dto/address';
import { Button } from '@mantine/core';
import { CourseAddStudentParentsList, CourseAddStudentStepper, CourseAddStudentSummary, CourseAddStudentUserForm } from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { IconArrowLeft, IconArrowRight, IconCheck, IconPlus } from '@tabler/icons';
import { StudentRegisterRequest, StudentRegisterResponse } from '../../../dto/student';
import { useForm } from '@mantine/form';
import { UserRegisterRequest, UserRole, UserSex } from '../../../dto/user';
import { useState } from 'react';
import { validatePesel } from '../../../validators/pesel';
import axios from 'axios';

const enum CourseAddStudentState {
  EDIT_STUDENT,
  EDIT_PARENTS,
  SUMMARY,
}

export type StudentRegisterFormData = {
  firstName: string;
  lastName: string;
  pesel?: string;
  sex?: UserSex;
  email?: string;
  phoneNumber?: string;
} & Address;

interface CourseAddStudentProps {
  course: CourseResponse;
  onStudentRegistered: (response: StudentRegisterResponse) => void;
}

export const CourseAddStudent = (
  { course: { id: courseId }, onStudentRegistered }: CourseAddStudentProps,
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
  const [studentFormData, setStudentFormData] = useState<StudentRegisterFormData>(userInitialValues);
  const [parentFormData, setParentFormData] = useState<StudentRegisterFormData>(userInitialValues);
  const [parentList, setParentList] = useState<StudentRegisterFormData[]>([]);
  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [registerResponse, setRegisterResponse] = useState<StudentRegisterResponse>();

  const form = useForm<StudentRegisterFormData>({
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

    setSendingRequest(true);

    axios.post(
      '/api/student', registerRequest,
    ).then((response) => {
      setModalState(CourseAddStudentState.SUMMARY);
      setSendingRequest(false);
      setRegisterResponse(response.data);
      onStudentRegistered(response.data);
    }).catch(() => {
      setSendingRequest(false);
      // TODO: handle failures!
    });
  };

  const buildUserRegisterRequest = (
    formData: StudentRegisterFormData,
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
      },
    };
  };

  return (
    <div className={'add-student'}>
      <CourseAddStudentStepper step={modalState.valueOf()}/>

      {/* TODO: edit already added parent */}
      {modalState === CourseAddStudentState.EDIT_PARENTS
        && parentList.length > 0 && (
          <CourseAddStudentParentsList
            parents={parentList}
            removeCallback={removeFromParentsList}
          />
        )}

      {modalState !== CourseAddStudentState.SUMMARY && (
        <CourseAddStudentUserForm
          formData={form}
          disabled={isUserFormDisabled()}
        />)}

      {modalState === CourseAddStudentState.SUMMARY &&
        <CourseAddStudentSummary registerResponse={registerResponse}/>}

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
          <>
            <Button
              leftIcon={<IconArrowLeft size={18}/>}
              variant={'outline'}
              onClick={openEditStudentView}
            >
              Dane ucznia
            </Button>
            {parentList.length < 2 && (
              <Button
                leftIcon={<IconPlus size={18}/>}
                variant={'outline'}
                onClick={addToParentsList}
              >
                Dodaj rodzica
              </Button>)}
            {parentList.length >= 1 && (
              <Button
                leftIcon={<IconCheck size={18}/>}
                onClick={sendRegisterRequest}
                loading={sendingRequest}
              >
                Dodaj ucznia do klasy
              </Button>)}
          </>)}
      </div>
    </div>
  );
};
