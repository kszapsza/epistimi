import NoAccountsIcon from '@mui/icons-material/NoAccounts';
import { Button, ButtonStyle } from '../Button';
import { HashLink } from 'react-router-hash-link';
import { Link } from 'react-router-dom';
import './Header.scss';

export const Header = (): JSX.Element => {
  return (
    <header className={'navbar'}>
      <div className={'navbar-wrapper'}>
        <h1 className={'navbar-logo'}>
          <Link onClick={() => window.scroll(0, 0)} to={'/'}>
            Epistimi
          </Link>
        </h1>
        <div className={'navbar-user'}>
          <div className={'navbar-login'}>
            <HashLink smooth to={'#login'}>
              <Button style={ButtonStyle.LIGHT}>
                Zaloguj się
              </Button>
            </HashLink>
          </div>
          <NoAccountsIcon/>
        </div>
      </div>
    </header>
  )
}
