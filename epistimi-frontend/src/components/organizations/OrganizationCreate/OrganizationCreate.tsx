import './OrganizationCreate.scss';
import { Alert, Button, TextInput } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { OrganizationRegisterRequest, OrganizationResponse } from '../../../dto/organization';
import { useForm } from '@mantine/form';
import { UserRole, UserSex } from '../../../dto/user';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface OrganizationEditProps {
  submitCallback: (organization: OrganizationResponse) => void;
  organizationId?: string;
}

type OrganizationEditForm = {
  organizationName: string;
  adminFirstName: string;
  adminLastName: string;
  adminRole: UserRole;
  adminPesel?: string;
  sex?: UserSex;
  adminEmail?: string;
  adminPhoneNumber?: string;
  adminStreet: string;
  adminPostalCode: string;
  adminCity: string;
  organizationStreet: string;
  organizationPostalCode: string;
  organizationCity: string;
};

export const OrganizationCreate = (props: OrganizationEditProps): JSX.Element => {
  const form = useForm<OrganizationEditForm>({
    initialValues: {
      organizationName: '',
      adminFirstName: '',
      adminLastName: '',
      adminRole: UserRole.ORGANIZATION_ADMIN,
      adminPesel: '',
      adminEmail: '',
      adminPhoneNumber: '',
      adminStreet: '',
      adminPostalCode: '',
      adminCity: '',
      organizationStreet: '',
      organizationPostalCode: '',
      organizationCity: '',
    },
    validate: (values) => ({
      name: values.organizationName === '' ? 'Podaj nazwę placówki' : null,
      street: !values.adminStreet ? 'Podaj ulicę' : null,
      postalCode: !values.adminPostalCode ? 'Podaj kod pocztowy' : null,
      city: !values.adminCity ? 'Podaj miasto' : null,
    }),
  });

  const [submitError, setSubmitError] = useState<string | null>(null);

  const hasErrors = (): boolean => {
    return !!form.errors.name
      || !!form.errors.adminId
      || !!form.errors.directorId
      || !!form.errors.address;
  };

  const submitHandler = (formData: OrganizationEditForm): void => {
    axios.post<OrganizationResponse, AxiosResponse<OrganizationResponse>, OrganizationRegisterRequest>(
      '/api/organization', {
        name: formData.organizationName,
        admin: {
          firstName: formData.adminFirstName,
          lastName: formData.adminLastName,
          role: formData.adminRole,
          pesel: formData.adminPesel,
          sex: formData.sex,
          email: formData.adminEmail,
          phoneNumber: formData.adminPhoneNumber,
          address: {
            street: formData.adminStreet,
            postalCode: formData.adminPostalCode,
            city: formData.adminCity,
          },
        },
        address: {
          street: formData.organizationStreet,
          postalCode: formData.organizationPostalCode,
          city: formData.organizationCity,
        },
      }).then((response) => {
      props.submitCallback(response.data);
    }).catch(({ response }) => {
      setSubmitError(
        response?.data?.message == 'Provided admin is already managing other organization'
          ? 'Wybrany administrator zarządza już inną placówką'
          : 'Wystąpił nieoczekiwany błąd serwera',
      );
    });
  };

  return (
    <div className={'organization-create'}>
      {hasErrors() &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
          Wszystkie pola są wymagane
        </Alert>
      }
      {submitError &&
        <Alert icon={<IconAlertCircle size={16}/>} title={'Błąd'} color={'red'}>
          {submitError}
        </Alert>
      }

      <form
        noValidate
        className={'organization-create-form'}
        onSubmit={form.onSubmit(submitHandler)}
      >
        <TextInput
          required
          label={'Nazwa'}
          autoComplete={'organization'}
          autoFocus={true}
          {...form.getInputProps('organizationName')}/>

        {/*TODO: admin data form*/}

        <div className={'organization-create-section-heading'}>
          Adres
        </div>

        <TextInput
          required
          label={'Ulica'}
          autoComplete={'street-address'}
          id={'street'}
          {...form.getInputProps('adminStreet')}/>

        <div className={'organization-create-city'}>
          <TextInput
            required
            label={'Kod pocztowy'}
            autoComplete={'postal-code'}
            id={'postal-code'}
            style={{ flex: 1 }}
            {...form.getInputProps('adminPostalCode')}/>
          <TextInput
            required
            label={'Miasto'}
            autoComplete={'address-level2'}
            id={'city'}
            style={{ flex: 2 }}
            {...form.getInputProps('adminCity')}/>
        </div>

        <Button type={'submit'}>
          Utwórz
        </Button>
      </form>
    </div>
  );
};
