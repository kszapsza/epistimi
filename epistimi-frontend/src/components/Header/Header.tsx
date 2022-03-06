import './Header.scss';
import { AccountCircle, NoAccounts } from '@mui/icons-material';
import { Button, ButtonStyle } from '../Button';
import { HashLink } from 'react-router-hash-link';
import { Link } from 'react-router-dom';
import { removeCurrentUser, TOKEN_KEY } from '../../store/slices/authSlice';
import { useAppDispatch, useAppSelector } from '../../store/hooks';
import { UserRole } from '../../dto/user';

export const Header = (): JSX.Element => {
  const { isAuthenticated, user } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <header className={'header'}>
      <div className={'header-wrapper'}>
        <Link onClick={() => window.scroll(0, 0)}
              to={isAuthenticated ? '/app/summary' : '/'}>
          <div className={'header-logo-box'}>
            <h1 className={'header-logo'}>
              Epistimi
            </h1>
            {user?.role === UserRole.EPISTIMI_ADMIN
              && <span className={'header-role'}>&nbsp;admin</span>}
          </div>
        </Link>
        <div className={'header-user'}>
          {!isAuthenticated && (
            <>
              <div className={'header-login'}>
                <HashLink smooth to={'#login'}>
                  <Button style={ButtonStyle.LIGHT}>
                    Zaloguj się
                  </Button>
                </HashLink>
              </div>
              <NoAccounts/>
            </>
          )}
          {isAuthenticated && user && (
            <>
              <Link onClick={handleLogout} to={'/'}>
                <AccountCircle/>
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};
