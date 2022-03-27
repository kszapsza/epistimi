import './MenuUser.scss';
import { Logout, Settings } from '@mui/icons-material';
import { Menu } from '@mantine/core';
import { MenuUserButton } from '../MenuUserButton';
import { removeCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { useAppDispatch } from '../../../store/hooks';

export const MenuUser = (): JSX.Element => {
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <Menu className={'menu-user'} control={<MenuUserButton/>}>
      <Menu.Label>Konto</Menu.Label>
      <Menu.Item icon={<Settings style={{ fontSize: '14px' }}/>}>Ustawienia</Menu.Item>
      <Menu.Item icon={<Logout style={{ fontSize: '14px' }}/>} onClick={handleLogout}>Wyloguj się</Menu.Item>
    </Menu>
  );
};
