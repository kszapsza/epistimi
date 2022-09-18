import './OrganizationForm.scss';
import { Button, TextInput, Title } from '@mantine/core';
import { IconArrowRight, IconCheck } from '@tabler/icons';
import { OrganizationFormData } from '../OrganizationCreate';
import { UseFormReturnType } from '@mantine/form/lib/use-form';

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
  return (
    <form
      noValidate
      className={'organization-form'}
      onSubmit={form.onSubmit(onSubmit)}
    >
      <div className={'organization-form-group'}>
        <TextInput
          required
          label={'Nazwa'}
          autoComplete={'organization'}
          autoFocus={true}
          {...form.getInputProps('name')}/>
      </div>

      <Title order={3}>
        Adres placówki
      </Title>

      <div className={'organization-form-group'}>
        <TextInput
          required
          label={'Ulica'}
          autoComplete={'street-address'}
          id={'street'}
          {...form.getInputProps('street')}/>

        <div className={'organization-city'}>
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
      </div>

      <div className={'organization-actions'}>
        {variant === OrganizationFormVariant.CREATE &&
          <Button
            rightIcon={<IconArrowRight size={18}/>}
            variant={'outline'}
            type={'submit'}
          >
            Dane administratora placówki
          </Button>}
        {variant === OrganizationFormVariant.UPDATE &&
          <Button
            leftIcon={<IconCheck size={18}/>}
            type={'submit'}
            loading={sendingRequest}
          >
            Zaktualizuj dane placówki
          </Button>}
      </div>
    </form>
  );
};
