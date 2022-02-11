import './LoginForm.scss';
import { Button, ButtonStyle } from '../../shared';

const LoginForm = (): JSX.Element => {
  return (
    <div className={'login-form-box'}>
      <h2>Zaloguj się do Epistimi</h2>
      <form className={'login-form'}>
        <div className={'login-form-group'}>
          <label htmlFor={'username'}>Nazwa użytkownika:</label>
          <input id={'username'} type={'text'} autoFocus={true}/>
        </div>
        <div className={'login-form-group'}>
          <label htmlFor={'password'}>Hasło:</label>
          <input id={'password'} type={'password'}/>
        </div>
        <Button style={ButtonStyle.CONSTRUCTIVE}>
          Zaloguj
        </Button>
      </form>
    </div>
  )
};

export default LoginForm;
