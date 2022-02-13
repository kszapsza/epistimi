import { Button, ButtonStyle, MessageBox, MessageBoxStyle } from '../../shared';
import { UnpackNestedValue, useForm } from 'react-hook-form';
import './LoginForm.scss';

interface LoginFormData {
  username: string;
  password: string;
}

export const LoginForm = (): JSX.Element => {
  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>();

  const submitHandler = (data: UnpackNestedValue<LoginFormData>): void => {
    console.log(data);
    // TODO
  };

  return (
    <div className="login-form-box">
      <form
        className="login-form"
        onSubmit={handleSubmit(submitHandler)}
      >
        {(errors.username || errors.password) && <MessageBox
          style={MessageBoxStyle.WARNING}
          small={true}
        >
          Niepoprawne dane logowania!
        </MessageBox>}
        <div className="login-form-groups">
          <div className="login-form-group">
            <label htmlFor="username">Nazwa użytkownika:</label>
            <input
              autoComplete="username"
              autoFocus={true}
              id="username"
              type="text"
              {...register('username', { required: true })}
            />
          </div>
          <div className="login-form-group">
            <label htmlFor="password">Hasło:</label>
            <input
              autoComplete="current-password"
              id="password"
              type="password"
              {...register('password', { required: true })}
            />
          </div>
        </div>
        <Button style={ButtonStyle.CONSTRUCTIVE}>
          Zaloguj się
        </Button>
      </form>
    </div>
  )
};
