import './LoginForm.scss';
import { Alert, Button, LoadingOverlay, PasswordInput, TextInput } from '@mantine/core';
import { fetchCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { IconAlertCircle } from '@tabler/icons';
import { LoginFormData, LoginResponse } from '../../../dto/login';
import { useDispatch } from 'react-redux';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

export const LoginForm = (): JSX.Element => {
  const [loadingOverlay, setLoadingOverlay] = useState<boolean>(false);
  const [serverUnauthorized, setServerUnauthorized] = useState<boolean>(false);
  const [serverFailed, setServerFailed] = useState<boolean>(false);

  const { t } = useTranslation();
  const dispatch = useDispatch();

  const form = useForm<LoginFormData>({
    initialValues: {
      username: '',
      password: '',
    },
    validate: (values) => ({
      username: values.username.trim() === '' ? t('mainPage.loginForm.enterUsername') : null,
      password: values.password.trim() === '' ? t('mainPage.loginForm.enterPassword') : null,
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
            {t('mainPage.loginForm.incorrectCredentials')}
          </Alert>}
        {serverFailed &&
          <Alert icon={<IconAlertCircle size={16}/>} color="red">
            {t('mainPage.loginForm.connectionFailed')}
          </Alert>}

        <div className={'login-form-fields'}>
          <TextInput
            autoFocus
            required
            label={t('mainPage.loginForm.username')}
            autoComplete={'username'}
            {...form.getInputProps('username')}
          />
          <PasswordInput
            required
            label={t('mainPage.loginForm.password')}
            autoComplete={'current-password'}
            {...form.getInputProps('password')}
          />
        </div>

        <Button type={'submit'} variant={'filled'}>
          {t('mainPage.loginForm.logIn')}
        </Button>
      </form>
    </div>
  );
};
