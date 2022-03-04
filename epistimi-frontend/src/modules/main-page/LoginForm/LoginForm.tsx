import './LoginForm.scss';
import { Button, ButtonStyle, MessageBox, MessageBoxStyle } from '../../../components';
import { LoginFormData, LoginResponse } from '../../../dto/login';
import { TOKEN_KEY, fetchCurrentUser } from '../../../store/slices/authSlice';
import { useDispatch } from 'react-redux';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import axios, { AxiosResponse } from 'axios';

export const LoginForm = (): JSX.Element => {
  const [serverUnauthorized, setServerUnauthorized] = useState<boolean>(false);
  const [serverFailed, setServerFailed] = useState<boolean>(false);
  const [showPassword, setShowPassword] = useState<boolean>(false);

  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const hasErrors = (): boolean => {
    return !!(errors.username || errors.password || serverUnauthorized);
  };

  const handleLogin = (formData: LoginFormData): void => {
    axios.post<LoginResponse, AxiosResponse<LoginResponse>, LoginFormData>(
      'auth/login', formData,
    ).then((response: AxiosResponse<LoginResponse>) => {
      localStorage.setItem(TOKEN_KEY, response.data.token);
      setServerUnauthorized(false);
      setServerFailed(false);
      dispatch(fetchCurrentUser());
      navigate('/app/summary');
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
    <div className='login-form-box' id={'login'}>
      <form
        className='login-form'
        onSubmit={handleSubmit((formData) => handleLogin(formData))}
      >
        {hasErrors() &&
          <MessageBox style={MessageBoxStyle.WARNING} small={true} >
            Niepoprawne dane logowania
          </MessageBox>}
        {serverFailed &&
          <MessageBox style={MessageBoxStyle.WARNING} small={true}>
            Nie udało się połączyć z serwerem
          </MessageBox>}

        <div className={'login-form-groups'}>
          <div className={'login-form-group'}>
            <label htmlFor={'username'}>Nazwa użytkownika</label>
            <div className={'login-form-field'}>
              <input
                autoComplete={'username'}
                autoFocus={true}
                className={'login-form-input'}
                id={'username'}
                type={'text'}
                {...register('username', { required: true })}
              />
            </div>
          </div>

          <div className={'login-form-group'}>
            <label htmlFor={'password'}>Hasło</label>
            <div className={'login-form-field'}>
              <input
                autoComplete={'current-password'}
                className={'login-form-input'}
                id={'password'}
                type={showPassword ? 'text' : 'password'}
                {...register('password', { required: true })}
              />
              <div className={'login-form-show-password'} onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <VisibilityOffIcon style={{ width: '16px' }}/> : <VisibilityIcon style={{ width: '16px' }}/>}
              </div>
            </div>
          </div>
        </div>

        <Button style={ButtonStyle.PRIMARY}>
          Zaloguj się
        </Button>
      </form>
    </div>
  );
};
