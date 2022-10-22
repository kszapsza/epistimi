import './OrganizationForm.scss';
import { Button, TextInput, Title } from '@mantine/core';
import { IconArrowRight, IconCheck } from '@tabler/icons';
import { OrganizationFormData } from '../OrganizationCreate';
import { UseFormReturnType } from '@mantine/form';
import { useTranslation } from 'react-i18next';

export const enum OrganizationFormVariant {
  CREATE,
  UPDATE,
}

interface OrganizationCreateFormProps {
  variant: OrganizationFormVariant;
  form: UseFormReturnType<OrganizationFormData>;
  sendingRequest?: boolean;
  onSubmit: (data: OrganizationFormData) => void;
}

export const OrganizationForm = (
  { variant, form, onSubmit, sendingRequest }: OrganizationCreateFormProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <form
      noValidate
      className={'organization-form'}
      onSubmit={form.onSubmit(onSubmit)}
    >
      <div className={'organization-form-group'}>
        <TextInput
          required
          label={t('organizations.organizationForm.name')}
          autoComplete={'organization'}
          autoFocus={true}
          {...form.getInputProps('name')}/>
      </div>

      <Title order={3}>
        {t('organizations.organizationForm.organizationAddress')}
      </Title>

      <div className={'organization-form-group'}>
        <TextInput
          required
          label={t('organizations.organizationForm.street')}
          autoComplete={'street-address'}
          id={'street'}
          {...form.getInputProps('street')}/>

        <div className={'organization-city'}>
          <TextInput
            required
            label={t('organizations.organizationForm.postalCode')}
            autoComplete={'postal-code'}
            id={'postal-code'}
            style={{ flex: 1 }}
            {...form.getInputProps('postalCode')}/>
          <TextInput
            required
            label={t('organizations.organizationForm.city')}
            autoComplete={'address-level2'}
            id={'city'}
            style={{ flex: 2 }}
            {...form.getInputProps('city')}/>
        </div>
      </div>

      <div className={'organization-actions'}>
        {variant === OrganizationFormVariant.CREATE &&
          <Button
            rightIcon={<IconArrowRight size={18}/>}
            variant={'outline'}
            type={'submit'}
          >
            {t('organizations.organizationForm.organizationAdminData')}
          </Button>}
        {variant === OrganizationFormVariant.UPDATE &&
          <Button
            leftIcon={<IconCheck size={18}/>}
            type={'submit'}
            loading={sendingRequest}
          >
            {t('organizations.organizationForm.updateOrganizationData')}
          </Button>}
      </div>
    </form>
  );
};
