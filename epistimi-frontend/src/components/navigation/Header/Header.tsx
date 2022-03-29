import './Header.scss';
import { Burger, MediaQuery } from '@mantine/core';
import { Link } from 'react-router-dom';
import { Logout } from 'tabler-icons-react';
import { removeCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface HeaderProps {
  navbarOpened?: boolean;
  onBurgerClick?: () => void;
}

export const Header = (props: HeaderProps): JSX.Element => {
  const { isAuthenticated, user } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <header className={'header'}>
      <div className={'header-wrapper'}>
        {props.navbarOpened && props.onBurgerClick &&
          <MediaQuery largerThan={'sm'} styles={{ display: 'none' }}>
            <Burger
              color={'white'}
              mr={'xl'}
              onClick={props.onBurgerClick}
              opened={props.navbarOpened}
              size={'sm'}
            />
          </MediaQuery>}
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
          {isAuthenticated && user && (
            <>
              <Link onClick={handleLogout} to={'/'}>
                <div role={'button'} aria-label={'Log out'}>
                  <Logout/>
                </div>
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};
