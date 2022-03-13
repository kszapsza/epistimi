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
  const { formState: { errors }, handleSubmit, register } = useForm<OrganizationRegisterRequest>();
  const { data, loading } = useFetch<UsersResponse>('/api/user?role=ORGANIZATION_ADMIN');
  const [submitFailed, setSubmitFailed] = useState<boolean>(false);

  if (loading) {
    return <Spinner/>;
  }

  const hasErrors = () => {
    return !!errors.name || !!errors.adminId;
  };

  const onSave = (formData: OrganizationRegisterRequest) => {
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
          <input id={'name'} {...register('name', { required: true })}/>
        </div>

        <div className={'organization-create-form-group'}>
          <label htmlFor={'admin'}>Administrator</label>
          <select id={'admin'} {...register('adminId', { required: true })}>
            {data?.users.map((admin) => {
              return <option key={admin.id} value={admin.id}>{admin.lastName} {admin.firstName} ({admin.username})</option>;
            })}
          </select>
        </div>

        <Button style={ButtonStyle.CONSTRUCTIVE}>
          Utwórz
        </Button>
      </form>
    </div>
  );
};
