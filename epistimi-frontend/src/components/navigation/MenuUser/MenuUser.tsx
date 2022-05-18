import './MenuUser.scss';
import { Divider, Menu } from '@mantine/core';
import { IconCheck, IconLogout, IconSettings } from '@tabler/icons';
import { MenuUserButton } from '../MenuUserButton';
import { removeCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

export const MenuUser = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <Menu className={'menu-user'} control={<MenuUserButton/>}>
      {user?.role === UserRole.PARENT &&
        <>
          <Menu.Label>Wybierz ucznia</Menu.Label>
          <Menu.Item rightSection={<IconCheck size={16}/>}>Jan Kowalski</Menu.Item>
          <Menu.Item>Malwina Kowalska</Menu.Item>
          <Divider/>
        </>}

      <Menu.Label>Konto</Menu.Label>
      <Menu.Item icon={<IconSettings size={14}/>}>Ustawienia</Menu.Item>
      <Menu.Item icon={<IconLogout size={14}/>} onClick={handleLogout}>Wyloguj się</Menu.Item>
    </Menu>
  );
};
