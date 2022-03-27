import './LoginForm.scss';
import { Alert, Button, PasswordInput, TextInput } from '@mantine/core';
import { ErrorOutline } from '@mui/icons-material';
import { fetchCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { LoginFormData, LoginResponse } from '../../../dto/login';
import { useDispatch } from 'react-redux';
import { useForm } from '@mantine/form';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

export const LoginForm = (): JSX.Element => {
  const [serverUnauthorized, setServerUnauthorized] = useState<boolean>(false);
  const [serverFailed, setServerFailed] = useState<boolean>(false);

  const dispatch = useDispatch();
  const navigate = useNavigate();

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
      localStorage.setItem(TOKEN_KEY, response.data.token);
      navigate('/app/summary');
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
        {hasErrors() &&
          <Alert icon={<ErrorOutline style={{ fontSize: '16px' }}/>} color="red">
            Niepoprawne dane logowania
          </Alert>}
        {serverFailed &&
          <Alert icon={<ErrorOutline style={{ fontSize: '16px' }}/>} color="red">
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
