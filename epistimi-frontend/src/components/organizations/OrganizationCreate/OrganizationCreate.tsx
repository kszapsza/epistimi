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
import { useTranslation } from 'react-i18next';
import { validatePesel } from '../../../validators/pesel';
import axios, { AxiosResponse } from 'axios';

const enum OrganizationCreateStep {
  ORGANIZATION_DATA = 0,
  ORGANIZATION_ADMIN_DATA = 1,
  SUMMARY = 2,
}

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

interface OrganizationEditProps {
  submitCallback: (organization: OrganizationRegisterResponse) => void;
  organizationId?: string;
}

export const OrganizationCreate = ({ submitCallback }: OrganizationEditProps): JSX.Element => {
  const [step, setStep] = useState<OrganizationCreateStep>(OrganizationCreateStep.ORGANIZATION_DATA);
  const [createResponse, setCreateResponse] = useState<OrganizationRegisterResponse>();
  const [submitError, setSubmitError] = useState<string | null>(null);

  const { t } = useTranslation();

  const organizationForm = useForm<OrganizationFormData>({
    initialValues: {
      name: '',
      street: '',
      postalCode: '',
      city: '',
    },
    validate: (values) => ({
      name: values.name.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      street: values.street.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      postalCode: values.postalCode.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      city: values.city.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
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
      firstName: values.firstName.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      lastName: values.lastName.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      pesel: !values.pesel ? t('organizations.organizationCreate.requiredField')
        : !validatePesel(values.pesel) ? t('organizations.organizationCreate.invalidPesel') : null,
      street: values.street.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      postalCode: values.postalCode.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
      city: values.city.trim() === '' ? t('organizations.organizationCreate.requiredField') : null,
    }),
  });

  const submitHandler = (): void => {
    if (organizationForm.validate().hasErrors
      || organizationAdminForm.validate().hasErrors) {
      return;
    }
    axios.post<OrganizationRegisterResponse, AxiosResponse<OrganizationRegisterResponse>, OrganizationRegisterRequest>(
      '/api/organization', buildRequestBody())
      .then((response) => {
        setCreateResponse(response.data);
        setStep(OrganizationCreateStep.SUMMARY);
        submitCallback(response.data);
      }).catch(({ response }) => {
      setSubmitError(
        response?.data?.message == 'Provided admin is already managing other organization'
          ? t('organizations.organizationCreate.adminAlreadyManagingOtherOrganization')
          : t('organizations.organizationCreate.serverError'),
      );
    });
  };

  const buildRequestBody = (): OrganizationRegisterRequest => {
    return {
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
    };
  };

  return (
    <div className={'organization-create'}>
      {submitError &&
        <Alert icon={<IconAlertCircle size={16}/>} title={t('organizations.organizationCreate.error')} color={'red'}>
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
