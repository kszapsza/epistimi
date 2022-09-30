import './MenuUser.scss';
import { Divider, Menu } from '@mantine/core';
import { IconCheck, IconLogout, IconSettings } from '@tabler/icons';
import { MenuUserButton } from '../../navigation';
import { removeCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';
import { useTranslation } from 'react-i18next';

export const MenuUser = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <Menu className={'menu-user'} control={<MenuUserButton/>}>
      {user?.role === UserRole.PARENT &&
        <>
          {/* TODO: hardcoded select */}
          <Menu.Label>{t('navigation.menuUser.selectStudent')}</Menu.Label>
          <Menu.Item rightSection={<IconCheck size={16}/>}>Jan Kowalski</Menu.Item>
          <Menu.Item>Malwina Kowalska</Menu.Item>
          <Divider/>
        </>}

      <Menu.Label>{t('navigation.menuUser.account')}</Menu.Label>
      <Menu.Item icon={<IconSettings size={14}/>}>{t('navigation.menuUser.settings')}</Menu.Item>
      <Menu.Item icon={<IconLogout size={14}/>} onClick={handleLogout}>{t('navigation.menuUser.logOut')}</Menu.Item>
    </Menu>
  );
};
