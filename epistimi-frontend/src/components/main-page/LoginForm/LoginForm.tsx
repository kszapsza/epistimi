import './LoginForm.scss';
import { Alert, Button, LoadingOverlay, PasswordInput, TextInput } from '@mantine/core';
import { fetchCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { IconAlertCircle } from '@tabler/icons';
import { LoginFormData, LoginResponse } from '../../../dto/login';
import { useDispatch } from 'react-redux';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

export const LoginForm = (): JSX.Element => {
  const [loadingOverlay, setLoadingOverlay] = useState<boolean>(false);
  const [serverUnauthorized, setServerUnauthorized] = useState<boolean>(false);
  const [serverFailed, setServerFailed] = useState<boolean>(false);

  const dispatch = useDispatch();

  const form = useForm<LoginFormData>({
    initialValues: {
      username: '',
      password: '',
    },
    validate: (values) => ({
      username: values.username === '' ? 'Podaj nazwę użytkownika' : null,
      password: values.password === '' ? 'Podaj hasło' : null,
    }),
  });

  const hasErrors = (): boolean => {
    return !!(form.errors.username || form.errors.password || serverUnauthorized);
  };

  const handleLogin = (formData: LoginFormData): void => {
    axios.post<LoginResponse, AxiosResponse<LoginResponse>, LoginFormData>(
      'auth/login', formData,
    ).then((response: AxiosResponse<LoginResponse>) => {
      setLoadingOverlay(true);
      localStorage.setItem(TOKEN_KEY, response.data.token);
      dispatch(fetchCurrentUser());
    }).catch((error) => {
      if (error?.response?.status === 401) {
        setServerUnauthorized(true);
        setServerFailed(false);
      } else {
        setServerFailed(true);
      }
    });
  };

  return (
    <div className={'login-form-box'} id={'login'}>
      <form
        noValidate
        className={'login-form'}
        onSubmit={form.onSubmit((formData) => handleLogin(formData))}
      >
        <LoadingOverlay visible={loadingOverlay} />
        {hasErrors() &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            Niepoprawne dane logowania
          </Alert>}
        {serverFailed &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            Nie udało się połączyć z&nbsp;serwerem
          </Alert>}

        <div className={'login-form-fields'}>
          <TextInput
            required
            label="Nazwa użytkownika"
            autoComplete={'username'}
            autoFocus={true}
            {...form.getInputProps('username')}
          />
          <PasswordInput
            required
            label="Hasło"
            autoComplete={'current-password'}
            {...form.getInputProps('password')}
          />
        </div>

        <Button type={'submit'} variant={'filled'}>
          Zaloguj się
        </Button>
      </form>
    </div>
  );
};
