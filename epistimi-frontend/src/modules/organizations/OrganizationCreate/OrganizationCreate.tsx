import './OrganizationCreate.scss';
import { Button, ButtonStyle, MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { OrganizationRegisterRequest, OrganizationResponse } from '../../../dto/organization';
import { useFetch } from '../../../hooks/useFetch';
import { useForm } from 'react-hook-form';
import { UsersResponse } from '../../../dto/user';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

interface OrganizationCreateProps {
  onCreated: (organization: OrganizationResponse) => void;
}

export const OrganizationCreate = (props: OrganizationCreateProps): JSX.Element => {
  const {
    formState: { errors },
    handleSubmit,
    register,
  } = useForm<OrganizationRegisterRequest>({ defaultValues: { address: { countryCode: 'PL' } } });

  const { data: admins, loading: adminsLoading } = useFetch<UsersResponse>('/api/user?role=ORGANIZATION_ADMIN');
  const { data: directors, loading: directorsLoading } = useFetch<UsersResponse>('/api/user?role=ORGANIZATION_ADMIN&role=TEACHER');

  const [submitFailed, setSubmitFailed] = useState<boolean>(false);

  if (adminsLoading || directorsLoading) {
    return <Spinner/>;
  }

  const hasErrors = (): boolean => {
    return !!errors.name
      || !!errors.adminId
      || !!errors.directorId
      || !!errors.address;
  };

  const onSave = (formData: OrganizationRegisterRequest): void => {
    axios.post<OrganizationResponse, AxiosResponse<OrganizationResponse>, OrganizationRegisterRequest>(
      '/api/organization', formData,
    ).then((response) => {
      props.onCreated(response.data);
    }).catch(() => {
      setSubmitFailed(true);
    });
  };

  return (
    <div className={'organization-create'}>
      <h3>Tworzenie nowej placówki</h3>

      {hasErrors() &&
        <MessageBox style={MessageBoxStyle.WARNING}>
          Wszystkie pola są wymagane
        </MessageBox>
      }
      {submitFailed &&
        <MessageBox style={MessageBoxStyle.WARNING}>
          Błąd serwera
        </MessageBox>
      }

      <form
        className={'organization-create-form'}
        onSubmit={handleSubmit((formData) => onSave(formData))}
      >
        <div className={'organization-create-form-group'}>
          <label htmlFor={'name'}>Nazwa</label>
          <input autoComplete={'organization'} id={'name'} {...register('name', { required: true })}/>
        </div>

        <div className={'organization-create-form-group'}>
          <label htmlFor={'admin'}>Administrator</label>
          <select id={'admin'} {...register('adminId', { required: true })}>
            <option key={undefined} value={''}/>
            {admins?.users.map((admin) => {
              return <option key={admin.id} value={admin.id}>
                {admin.lastName} {admin.firstName} ({admin.username})
              </option>;
            })}
          </select>
        </div>

        <div className={'organization-create-form-group'}>
          <label htmlFor={'director'}>Dyrektor</label>
          <select id={'director'} {...register('directorId', { required: true })}>
            <option key={undefined} value={''}/>
            {directors?.users.map((director) => {
              return <option key={director.id} value={director.id}>
                {director.lastName} {director.firstName} ({director.username})
              </option>;
            })}
          </select>
        </div>

        <div className={'organization-create-section-heading'}>
          Adres
        </div>

        <div className={'organization-create-form-group'}>
          <label htmlFor={'street'}>Ulica</label>
          <input
            autoComplete={'street-address'}
            id={'street'}
            {...register('address.street', { required: true })}/>
        </div>
        <div className={'organization-create-form-group'}>
          <label htmlFor={'postal-code'}>Kod pocztowy</label>
          <input
            autoComplete={'postal-code'}
            id={'postal-code'}
            {...register('address.postalCode', { required: true })}/>
        </div>
        <div className={'organization-create-form-group'}>
          <label htmlFor={'city'}>Miasto</label>
          <input
            autoComplete={'address-level2'}
            id={'city'}
            {...register('address.city', { required: true })}/>
        </div>
        <div className={'organization-create-form-group'}>
          <label htmlFor={'country'}>Kraj</label>
          <input
            disabled={true}
            autoComplete={'off'}
            id={'country'}
            {...register('address.countryCode', { required: true })}/>
        </div>

        <Button style={ButtonStyle.CONSTRUCTIVE}>
          Utwórz
        </Button>
      </form>
    </div>
  );
};
