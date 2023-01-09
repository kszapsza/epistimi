import './MenuUser.scss';
import { IconLogout } from '@tabler/icons';
import { Menu } from '@mantine/core';
import { MenuUserButton } from '../../navigation';
import { removeCurrentUser, TOKEN_KEY } from '../../../store/slices/authSlice';
import { useAppDispatch } from '../../../store/hooks';
import { useTranslation } from 'react-i18next';

export const MenuUser = (): JSX.Element => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const handleLogout = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    dispatch(removeCurrentUser());
  };

  return (
    <Menu width={'100%'}>
      <Menu.Target>
        <MenuUserButton/>
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Label>{t('navigation.menuUser.account')}</Menu.Label>
        <Menu.Item icon={<IconLogout size={14}/>} onClick={handleLogout}>{t('navigation.menuUser.logOut')}</Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
};
