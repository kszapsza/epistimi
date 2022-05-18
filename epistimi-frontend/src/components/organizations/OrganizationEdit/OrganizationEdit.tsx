import './OrganizationEdit.scss';
import { Address } from '../../../dto/address';
import { Alert, Button, Loader, NativeSelect, TextInput } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { OrganizationRegisterRequest, OrganizationResponse } from '../../../dto/organization';
import { useFetch } from '../../../hooks/useFetch';
import { useForm } from '@mantine/form';
import { UsersResponse } from '../../../dto/user';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

export const enum OrganizationEditVariant {
  CREATE = 'CREATE',
  UPDATE = 'UPDATE',
}

interface OrganizationEditProps {
  submitCallback: (organization: OrganizationResponse) => void;
  variant: OrganizationEditVariant;
  organizationId?: string;
  defaults?: OrganizationResponse;
}

type OrganizationEditForm = {
  name: string;
  adminId: string;
  directorId: string;
} & Address;

export const OrganizationEdit = (props: OrganizationEditProps): JSX.Element => {
  const form = useForm<OrganizationEditForm>({
    initialValues: {
      name: props.defaults?.name ?? '',
      adminId: props.defaults?.admin.id ?? '',
      directorId: props.defaults?.director.id ?? '',
      street: props.defaults?.address.street ?? '',
      postalCode: props.defaults?.address.postalCode ?? '',
      city: props.defaults?.address.city ?? '',
      countryCode: props.defaults?.address.countryCode ?? 'PL',
    },
    validate: (values) => ({
      name: values.name === '' ? 'Podaj nazwę placówki' : null,
      adminId: !values.adminId ? 'Nie wybrano administratora placówki' : null,
      directorId: !values.directorId ? 'Nie wybrano dyrektora placówki' : null,
      street: !values.street ? 'Podaj ulicę' : null,
      postalCode: !values.postalCode ? 'Podaj kod pocztowy' : null,
      city: !values.city ? 'Podaj miasto' : null,
      countryCode: !values.countryCode ? 'Podaj kod pocztowy' : null,
    }),
  });

  const { data: admins, loading: adminsLoading } = useFetch<UsersResponse>('/api/user?role=ORGANIZATION_ADMIN');
  const { data: directors, loading: directorsLoading } = useFetch<UsersResponse>('/api/user?role=ORGANIZATION_ADMIN&role=TEACHER');

  const [submitError, setSubmitError] = useState<string | null>(null);

  if (adminsLoading || directorsLoading) {
    return <Loader/>;
  }

  const getButtonLabel = (): string => {
    switch (props.variant) {
      case OrganizationEditVariant.CREATE:
        return 'Utwórz';
      case OrganizationEditVariant.UPDATE:
        return 'Zapisz';
    }
  };

  const hasErrors = (): boolean => {
    return !!form.errors.name
      || !!form.errors.adminId
      || !!form.errors.directorId
      || !!form.errors.address;
  };

  const submitHandler = (formData: OrganizationEditForm): void => {
    const action =
      props.variant == OrganizationEditVariant.UPDATE
        ? axios.put
        : axios.post;
    const endpoint =
      props.variant == OrganizationEditVariant.UPDATE && props.organizationId
        ? `/api/organization/${props.organizationId}`
        : '/api/organization';

    action<OrganizationResponse, AxiosResponse<OrganizationResponse>, OrganizationRegisterRequest>(
      endpoint, {
        name: formData.name,
        adminId: formData.adminId,
        directorId: formData.directorId,
        address: {
          street: formData.street,
          postalCode: formData.postalCode,
          city: formData.city,
          countryCode: formData.countryCode,
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

      {admins && directors &&
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
            {...form.getInputProps('name')}/>

          {/* TODO: admin/director user create form instead of dropdown select?
               (or prepare users EPISTIMI_ADMIN create form) */}
          <NativeSelect
            required
            label={'Administrator'}
            placeholder={'Wybierz administratora placówki'}
            data={
              admins.users.map((admin) =>
                ({ value: admin.id, label: `${admin.lastName} ${admin.firstName} (${admin.username})` }))
            }
            {...form.getInputProps('adminId')}
          />
          <NativeSelect
            required
            label={'Dyrektor'}
            placeholder={'Wybierz dyrektora placówki'}
            data={
              directors.users.map((director) =>
                ({ value: director.id, label: `${director.lastName} ${director.firstName} (${director.username})` }))
            }
            {...form.getInputProps('directorId')}
          />

          <div className={'organization-create-section-heading'}>
            Adres
          </div>

          <TextInput
            required
            label={'Ulica'}
            autoComplete={'street-address'}
            id={'street'}
            {...form.getInputProps('street')}/>

          <div className={'organization-create-city'}>
            <TextInput
              required
              label={'Kod pocztowy'}
              autoComplete={'postal-code'}
              id={'postal-code'}
              style={{ flex: 1 }}
              {...form.getInputProps('postalCode')}/>
            <TextInput
              required
              label={'Miasto'}
              autoComplete={'address-level2'}
              id={'city'}
              style={{ flex: 2 }}
              {...form.getInputProps('city')}/>
          </div>

          <TextInput
            required
            label={'Kraj'}
            disabled={true}
            autoComplete={'off'}
            id={'country'}
            {...form.getInputProps('countryCode')}/>

          <Button type={'submit'}>
            {getButtonLabel()}
          </Button>
        </form>}
    </div>
  );
};
