import './OrganizationAdminForm.scss';
import { Button, Radio, RadioGroup, TextInput, Title } from '@mantine/core';
import { IconArrowLeft, IconPlus } from '@tabler/icons';
import { OrganizationAdminFormData } from '../OrganizationCreate';
import { UseFormReturnType } from '@mantine/form/lib/use-form';

interface OrganizationAdminFormProps {
  form: UseFormReturnType<OrganizationAdminFormData>;
  onPrev: () => void;
  onNext: () => void;
}

export const OrganizationAdminForm = (
  { form, onPrev, onNext }: OrganizationAdminFormProps,
): JSX.Element => {
  return (
    <form
      noValidate
      className={'organization-admin-form'}
      onSubmit={form.onSubmit(onNext)}
    >
      <div className={'organization-admin-form-group'}>
        <TextInput
          required
          label={'Imię'}
          {...form.getInputProps('firstName')}
        />
        <TextInput
          required
          label={'Nazwisko'}
          {...form.getInputProps('lastName')}
        />
        <TextInput
          required
          label={'PESEL'}
          {...form.getInputProps('pesel')}
        />

        <RadioGroup
          label={'Płeć'}
          {...form.getInputProps('sex')}
        >
          <Radio size={'xs'} value={'FEMALE'} label={'Kobieta'}/>
          <Radio size={'xs'} value={'MALE'} label={'Mężczyzna'}/>
          <Radio size={'xs'} value={'OTHER'} label={'Inna'}/>
        </RadioGroup>

        <TextInput
          label={'E-mail'}
          {...form.getInputProps('email')}
        />
        <TextInput
          label={'Numer telefonu'}
          {...form.getInputProps('phoneNumber')}
        />
      </div>

      <Title order={3}>
        Adres
      </Title>

      <div className={'organization-admin-form-group'}>
        <TextInput
          required
          label={'Ulica'}
          autoComplete={'street-address'}
          id={'street'}
          {...form.getInputProps('street')}/>

        <div className={'organization-admin-city'}>
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

      <div className={'organization-create-actions'}>
        <Button
          leftIcon={<IconArrowLeft size={18}/>}
          variant={'outline'}
          onClick={onPrev}
        >
          Dane placówki
        </Button>
        <Button
          leftIcon={<IconPlus size={18}/>}
          type={'submit'}
        >
          Utwórz placówkę
        </Button>
      </div>
    </form>
  );
};
