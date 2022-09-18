import './OrganizationCreate.scss';
import { Alert } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import {
  OrganizationAdminForm,
  OrganizationCreateStepper,
  OrganizationCreateSummary,
  OrganizationForm,
  OrganizationFormVariant,
} from '../../organizations';
import { OrganizationRegisterRequest, OrganizationRegisterResponse } from '../../../dto/organization';
import { useForm } from '@mantine/form';
import { UserRole, UserSex } from '../../../dto/user';
import { useState } from 'react';
import { validatePesel } from '../../../validators/pesel';
import axios, { AxiosResponse } from 'axios';

export interface OrganizationFormData {
  name: string;
  street: string;
  postalCode: string;
  city: string;
}

export interface OrganizationAdminFormData {
  firstName: string;
  lastName: string;
  pesel: string;
  sex?: UserSex;
  email?: string;
  phoneNumber?: string;
  street: string;
  postalCode: string;
  city: string;
}

const enum OrganizationCreateStep {
  ORGANIZATION_DATA,
  ORGANIZATION_ADMIN_DATA,
  SUMMARY,
}

interface OrganizationEditProps {
  submitCallback: (organization: OrganizationRegisterResponse) => void;
  organizationId?: string;
}

export const OrganizationCreate = (props: OrganizationEditProps): JSX.Element => {
  const [step, setStep] = useState<OrganizationCreateStep>(OrganizationCreateStep.ORGANIZATION_DATA);
  const [createResponse, setCreateResponse] = useState<OrganizationRegisterResponse>();
  const [submitError, setSubmitError] = useState<string | null>(null);

  const organizationForm = useForm<OrganizationFormData>({
    initialValues: {
      name: '',
      street: '',
      postalCode: '',
      city: '',
    },
    validate: (values) => ({
      name: values.name.trim() === '' ? 'Wymagane pole' : null,
      street: values.street.trim() === '' ? 'Wymagane pole' : null,
      postalCode: values.postalCode.trim() === '' ? 'Wymagane pole' : null,
      city: values.city.trim() === '' ? 'Wymagane pole' : null,
    }),
  });

  const organizationAdminForm = useForm<OrganizationAdminFormData>({
    initialValues: {
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
      firstName: values.firstName.trim() === '' ? 'Wymagane pole' : null,
      lastName: values.lastName.trim() === '' ? 'Wymagane pole' : null,
      pesel: !values.pesel ? 'Wymagane pole' : !validatePesel(values.pesel) ? 'Niepoprawny PESEL' : null,
      street: values.street.trim() === '' ? 'Wymagane pole' : null,
      postalCode: values.postalCode.trim() === '' ? 'Wymagane pole' : null,
      city: values.city.trim() === '' ? 'Wymagane pole' : null,
    }),
  });

  const submitHandler = (): void => {
    if (organizationForm.validate().hasErrors
      || organizationAdminForm.validate().hasErrors) {
      return;
    }
    axios.post<OrganizationRegisterResponse, AxiosResponse<OrganizationRegisterResponse>, OrganizationRegisterRequest>(
      '/api/organization', {
        name: organizationForm.values.name,
        admin: {
          firstName: organizationAdminForm.values.firstName,
          lastName: organizationAdminForm.values.lastName,
          role: UserRole.ORGANIZATION_ADMIN,
          pesel: organizationAdminForm.values.pesel,
          sex: organizationAdminForm.values.sex,
          email: organizationAdminForm.values.email,
          phoneNumber: organizationAdminForm.values.phoneNumber,
          address: {
            street: organizationAdminForm.values.street,
            postalCode: organizationAdminForm.values.postalCode,
            city: organizationAdminForm.values.city,
          },
        },
        address: {
          street: organizationForm.values.street,
          postalCode: organizationForm.values.postalCode,
          city: organizationForm.values.city,
        },
      }).then((response) => {
      setCreateResponse(response.data);
      setStep(OrganizationCreateStep.SUMMARY);
      props.submitCallback(response.data);
    }).catch(({ response }) => {
      setSubmitError(
        response?.data?.message == 'Provided admin is already managing other organization'
          ? 'Wybrany administrator zarządza już inną placówką'
          : 'Wystąpił nieoczekiwany błąd serwera', // TODO: update same as in organization update
      );
    });
  };

  return (
    <div className={'organization-create'}>
      {submitError &&
        <Alert icon={<IconAlertCircle size={16}/>} title={'Błąd'} color={'red'}>
          {submitError}
        </Alert>}

      <OrganizationCreateStepper step={step.valueOf()}/>

      {step === OrganizationCreateStep.ORGANIZATION_DATA &&
        <OrganizationForm
          variant={OrganizationFormVariant.CREATE}
          form={organizationForm}
          onSubmit={() => setStep(OrganizationCreateStep.ORGANIZATION_ADMIN_DATA)}/>}

      {step === OrganizationCreateStep.ORGANIZATION_ADMIN_DATA &&
        <OrganizationAdminForm
          form={organizationAdminForm}
          onPrev={() => setStep(OrganizationCreateStep.ORGANIZATION_DATA)}
          onNext={() => submitHandler()}/>}

      {step === OrganizationCreateStep.SUMMARY && createResponse &&
        <OrganizationCreateSummary newOrganization={createResponse}/>}
    </div>
  );
};
