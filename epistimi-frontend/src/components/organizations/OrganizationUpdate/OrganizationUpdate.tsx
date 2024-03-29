import './OrganizationUpdate.scss';
import { Alert } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { OrganizationForm, OrganizationFormData, OrganizationFormVariant } from '../../organizations';
import { OrganizationResponse, OrganizationUpdateRequest } from '../../../dto/organization';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

interface OrganizationUpdateProps {
  submitCallback: (response: OrganizationResponse) => void;
  updatedOrganization: OrganizationResponse;
}

export const OrganizationUpdate = (
  { updatedOrganization, submitCallback }: OrganizationUpdateProps,
): JSX.Element => {
  const [sendingRequest, setSendingRequest] = useState<boolean>(false);
  const [submitError, setSubmitError] = useState<boolean>(false);

  const { t } = useTranslation();

  const form = useForm<OrganizationFormData>({
    initialValues: {
      name: updatedOrganization.name,
      street: updatedOrganization.address.street,
      postalCode: updatedOrganization.address.postalCode,
      city: updatedOrganization.address.city,
    },
    validate: (values) => ({
      name: values.name.trim() === '' ? t('organizations.organizationUpdate.requiredField') : null,
      street: values.street.trim() === '' ? t('organizations.organizationUpdate.requiredField') : null,
      postalCode: values.postalCode.trim() === '' ? t('organizations.organizationUpdate.requiredField') : null,
      city: values.city.trim() === '' ? t('organizations.organizationUpdate.requiredField') : null,
    }),
  });

  const onSubmit = (): void => {
    setSendingRequest(true);
    axios.put<OrganizationResponse, AxiosResponse<OrganizationResponse>, OrganizationUpdateRequest>(
      `/api/organization/${updatedOrganization.id}`, {
        name: form.values.name,
        address: {
          street: form.values.street,
          postalCode: form.values.postalCode,
          city: form.values.city,
        },
      },
    ).then((response) => {
      setSendingRequest(false);
      setSubmitError(false);
      submitCallback(response.data);
    }).catch(() => {
      setSendingRequest(false);
      setSubmitError(true);
    });
  };

  return (
    <div className={'organization-update'}>
      {submitError &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={t('organizations.organizationUpdate.serverError')}>
          {t('organizations.organizationUpdate.couldNotUpdateOrganization')}
        </Alert>}
      <OrganizationForm
        variant={OrganizationFormVariant.UPDATE}
        form={form}
        onSubmit={onSubmit}
        sendingRequest={sendingRequest}/>
    </div>
  );
};
