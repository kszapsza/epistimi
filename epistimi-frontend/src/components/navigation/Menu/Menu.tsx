import './Menu.scss';
import {
  IconBook,
  IconBriefcase,
  IconBuilding,
  IconHome,
  IconSpeakerphone,
  IconStars,
  IconUsers,
} from '@tabler/icons';
import { MenuItem, MenuUser } from '../../navigation';
import { Navbar, ScrollArea } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';
import { useTranslation } from 'react-i18next';

interface MenuProps {
  onMenuItemClick: () => void;
}

export const Menu = (props: MenuProps): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { t } = useTranslation();

  let menu: JSX.Element;

  switch (user?.role) {
    case UserRole.EPISTIMI_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBuilding/>} label={t('navigation.menu.organizations')} href={'/app/organizations'}/>
        </div>
      );
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconUsers/>} label={t('navigation.menu.courses')} href={'/app/courses'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBriefcase/>} label={t('navigation.menu.teachers')} href={'/app/teachers'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>
      );
      break;
    case UserRole.TEACHER:
      menu =
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>;
      break;
    case UserRole.STUDENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStars/>} label={t('navigation.menu.grades')} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>
      );
      break;
    case UserRole.PARENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStars/>} label={t('navigation.menu.grades')} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>
      );
      break;
    default:
      menu = <></>;
  }

  return (
    <div className={'menu'}>
      <Navbar.Section grow component={ScrollArea} mx="-xs" px="xs">
        {menu}
      </Navbar.Section>
      <Navbar.Section>
        <MenuUser/>
      </Navbar.Section>
    </div>
  );
};
