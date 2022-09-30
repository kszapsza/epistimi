import './OrganizationAdminForm.scss';
import { Button, Radio, RadioGroup, TextInput, Title } from '@mantine/core';
import { IconArrowLeft, IconPlus } from '@tabler/icons';
import { OrganizationAdminFormData } from '../OrganizationCreate';
import { UseFormReturnType } from '@mantine/form/lib/use-form';
import { useTranslation } from 'react-i18next';

interface OrganizationAdminFormProps {
  form: UseFormReturnType<OrganizationAdminFormData>;
  onPrev: () => void;
  onNext: () => void;
}

export const OrganizationAdminForm = (
  { form, onPrev, onNext }: OrganizationAdminFormProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <form
      noValidate
      className={'organization-admin-form'}
      onSubmit={form.onSubmit(onNext)}
    >
      <div className={'organization-admin-form-group'}>
        <TextInput
          required
          label={t('organizations.organizationAdminForm.firstName')}
          {...form.getInputProps('firstName')}
        />
        <TextInput
          required
          label={t('organizations.organizationAdminForm.lastName')}
          {...form.getInputProps('lastName')}
        />
        <TextInput
          required
          label={t('organizations.organizationAdminForm.pesel')}
          {...form.getInputProps('pesel')}
        />

        <RadioGroup
          label={t('organizations.organizationAdminForm.sex')}
          {...form.getInputProps('sex')}
        >
          <Radio size={'xs'} value={'FEMALE'} label={t('organizations.organizationAdminForm.sexFemale')}/>
          <Radio size={'xs'} value={'MALE'} label={t('organizations.organizationAdminForm.sexMale')}/>
          <Radio size={'xs'} value={'OTHER'} label={t('organizations.organizationAdminForm.sexOther')}/>
        </RadioGroup>

        <TextInput
          label={t('organizations.organizationAdminForm.email')}
          {...form.getInputProps('email')}
        />
        <TextInput
          label={t('organizations.organizationAdminForm.phoneNumber')}
          {...form.getInputProps('phoneNumber')}
        />
      </div>

      <Title order={3}>
        {t('organizations.organizationAdminForm.address')}
      </Title>

      <div className={'organization-admin-form-group'}>
        <TextInput
          required
          label={t('organizations.organizationAdminForm.street')}
          autoComplete={'street-address'}
          id={'street'}
          {...form.getInputProps('street')}/>

        <div className={'organization-admin-city'}>
          <TextInput
            required
            label={t('organizations.organizationAdminForm.postalCode')}
            autoComplete={'postal-code'}
            id={'postal-code'}
            style={{ flex: 1 }}
            {...form.getInputProps('postalCode')}/>
          <TextInput
            required
            label={t('organizations.organizationAdminForm.city')}
            autoComplete={'address-level2'}
            id={'city'}
            style={{ flex: 2 }}
            {...form.getInputProps('city')}/>
        </div>
      </div>

      <div className={'organization-create-actions'}>
        <Button
          leftIcon={<IconArrowLeft size={18}/>}
          variant={'outline'}
          onClick={onPrev}
        >
          {t('organizations.organizationAdminForm.organizationData')}
        </Button>
        <Button
          leftIcon={<IconPlus size={18}/>}
          type={'submit'}
        >
          {t('organizations.organizationAdminForm.createOrganization')}
        </Button>
      </div>
    </form>
  );
};
