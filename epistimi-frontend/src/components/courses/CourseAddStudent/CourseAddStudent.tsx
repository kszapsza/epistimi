import './CourseAddStudent.scss';
import { Address } from '../../../dto/address';
import { Alert } from '@mantine/core';
import {
  CourseAddStudentActions,
  CourseAddStudentParentsList,
  CourseAddStudentStepper,
  CourseAddStudentSummary,
  CourseAddStudentUserForm,
} from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { IconAlertCircle } from '@tabler/icons';
import { StudentRegisterRequest, StudentRegisterResponse } from '../../../dto/student';
import { useForm } from '@mantine/form';
import { useReducer } from 'react';
import { UserRegisterRequest, UserRole, UserSex } from '../../../dto/user';
import { useTranslation } from 'react-i18next';
import { validatePesel } from '../../../validators/pesel';
import axios from 'axios';

export const enum CourseAddStudentStep {
  EDIT_STUDENT = 0,
  EDIT_PARENTS = 1,
  SUMMARY = 2,
}

export type UserFormData = {
  firstName: string;
  lastName: string;
  pesel?: string;
  sex?: UserSex;
  email?: string;
  phoneNumber?: string;
} & Address;

type CourseAddStudentState = {
  step: CourseAddStudentStep;
  studentFormData: UserFormData;
  parentFormData: UserFormData;
  parentList: UserFormData[];
  sendingRequest: boolean;
  submittingError: boolean;
  registerResponse?: StudentRegisterResponse;
};

type CourseAddStudentAction =
  | { type: 'OPEN_EDIT_PARENTS', studentFormData: UserFormData }
  | { type: 'OPEN_EDIT_STUDENT', parentFormData: UserFormData }
  | { type: 'ADD_TO_PARENTS_LIST', newParent: UserFormData }
  | { type: 'REMOVE_FROM_PARENTS_LIST', parentIndex: number }
  | { type: 'SUBMIT_INIT' }
  | { type: 'SUBMIT_SUCCESS', registerResponse: StudentRegisterResponse }
  | { type: 'SUBMIT_ERROR' };

type CourseAddStudentProps = {
  course: CourseResponse;
  onStudentRegistered: (response: StudentRegisterResponse) => void;
}

const courseAddStudentReducer = (
  state: CourseAddStudentState,
  action: CourseAddStudentAction,
): CourseAddStudentState => {
  switch (action.type) {
    case 'OPEN_EDIT_PARENTS':
      return {
        ...state,
        step: CourseAddStudentStep.EDIT_PARENTS,
        studentFormData: action.studentFormData,
      };
    case 'OPEN_EDIT_STUDENT':
      return {
        ...state,
        step: CourseAddStudentStep.EDIT_STUDENT,
        parentFormData: action.parentFormData,
      };
    case 'ADD_TO_PARENTS_LIST':
      return {
        ...state,
        parentList: [...state.parentList, action.newParent],
      };
    case 'REMOVE_FROM_PARENTS_LIST':
      return {
        ...state,
        parentList: state.parentList
          .filter((_, index) => index !== action.parentIndex),
      };
    case 'SUBMIT_INIT':
      return {
        ...state,
        submittingError: false,
        sendingRequest: true,
      };
    case 'SUBMIT_SUCCESS':
      return {
        ...state,
        step: CourseAddStudentStep.SUMMARY,
        sendingRequest: false,
        registerResponse: action.registerResponse,
      };
    case 'SUBMIT_ERROR':
      return {
        ...state,
        submittingError: true,
        sendingRequest: false,
      };
  }
};

export const CourseAddStudent = (
  { course: { id: courseId }, onStudentRegistered }: CourseAddStudentProps,
): JSX.Element => {

  const USER_FORM_INITIAL_VALUES = {
    firstName: '',
    lastName: '',
    pesel: '',
    email: '',
    phoneNumber: '',
    street: '',
    postalCode: '',
    city: '',
  };

  const [{
    step,
    studentFormData,
    parentFormData,
    parentList,
    sendingRequest,
    submittingError,
    registerResponse,
  }, dispatch] = useReducer(courseAddStudentReducer, {
    step: CourseAddStudentStep.EDIT_STUDENT,
    studentFormData: USER_FORM_INITIAL_VALUES,
    parentFormData: USER_FORM_INITIAL_VALUES,
    parentList: [],
    sendingRequest: false,
    submittingError: false,
    registerResponse: undefined,
  });

  const { t } = useTranslation();

  const form = useForm<UserFormData>({
    initialValues: {
      ...USER_FORM_INITIAL_VALUES,
    },
    validate: (values) => ({
      firstName: !values.firstName ? t('courses.courseAddStudent.requiredField') : null,
      lastName: !values.lastName ? t('courses.courseAddStudent.requiredField') : null,
      pesel: !values.pesel ? t('courses.courseAddStudent.requiredField')
        : !validatePesel(values.pesel) ? t('courses.courseAddStudent.invalidPesel') : null,
    }),
  });

  const openEditParentsView = (): void => {
    if (form.validate().hasErrors) return;
    dispatch({ type: 'OPEN_EDIT_PARENTS', studentFormData: form.values });

    form.clearErrors();
    form.setValues(parentFormData);
  };

  const openEditStudentView = (): void => {
    dispatch({ type: 'OPEN_EDIT_STUDENT', parentFormData: form.values });

    form.clearErrors();
    form.setValues(studentFormData);
  };

  const addToParentsList = (): void => {
    if (form.validate().hasErrors) return;
    dispatch({ type: 'ADD_TO_PARENTS_LIST', newParent: form.values });

    form.clearErrors();
    form.setValues(USER_FORM_INITIAL_VALUES);
  };

  const removeFromParentsList = (idx: number): void => {
    dispatch({ type: 'REMOVE_FROM_PARENTS_LIST', parentIndex: idx });
  };

  const isUserFormDisabled = (): boolean => {
    return step === CourseAddStudentStep.EDIT_PARENTS && parentList.length === 2;
  };

  const sendRegisterRequest = (): void => {
    dispatch({ type: 'SUBMIT_INIT' });

    axios.post(
      '/api/student', buildStudentRegisterRequest(),
    ).then((response): void => {
      dispatch({ type: 'SUBMIT_SUCCESS', registerResponse: response.data });
      onStudentRegistered(response.data);
    }).catch((): void => {
      dispatch({ type: 'SUBMIT_ERROR' });
    });
  };

  const buildStudentRegisterRequest = (): StudentRegisterRequest => {
    const buildUserRegisterRequest = (
      formData: UserFormData,
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
        address: buildUserAddress(formData),
      };
    };

    const buildUserAddress = ({ street, postalCode, city }: UserFormData): Address | undefined => {
      if (street && postalCode && city) {
        return { street, postalCode, city };
      }
      return undefined;
    };

    return {
      courseId,
      user: buildUserRegisterRequest(studentFormData, UserRole.STUDENT),
      parents: parentList.map((parentFormData) =>
        buildUserRegisterRequest(parentFormData, UserRole.PARENT)),
    };
  };

  return (
    <div className={'add-student'}>
      <CourseAddStudentStepper step={step.valueOf() as number}/>

      {submittingError &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
          {t('courses.courseAddStudent.errorAddingStudent')}
        </Alert>}

      {step === CourseAddStudentStep.EDIT_PARENTS
        && parentList.length > 0 && (
          <CourseAddStudentParentsList
            parents={parentList}
            removeCallback={removeFromParentsList}
          />
        )}

      {step !== CourseAddStudentStep.SUMMARY && (
        <CourseAddStudentUserForm
          formData={form}
          disabled={isUserFormDisabled()}
        />)}

      {step === CourseAddStudentStep.SUMMARY &&
        <CourseAddStudentSummary registerResponse={registerResponse}/>}

      <CourseAddStudentActions
        modalStep={step}
        onAddParentClick={addToParentsList}
        onEditParentsClick={openEditParentsView}
        onEditStudentClick={openEditStudentView}
        onSubmitClick={sendRegisterRequest}
        parentsList={parentList}
        sendingRequest={sendingRequest}
      />
    </div>
  );
};
