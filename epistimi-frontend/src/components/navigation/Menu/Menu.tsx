import './Menu.scss';
import {
  IconBook,
  IconBriefcase,
  IconBuilding,
  IconCalendar,
  IconClock,
  IconDeviceDesktopAnalytics,
  IconFileText,
  IconHandTwoFingers,
  IconHome,
  IconMessageDots,
  IconMessages,
  IconSchool,
  IconSettings,
  IconSpeakerphone,
  IconStars,
  IconUsers,
  IconWriting,
  IconZoomQuestion,
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
          <MenuItem onClick={props.onMenuItemClick} icon={<IconUsers/>} label={t('navigation.menu.users')} href={'/app/users'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconFileText/>} label={t('navigation.menu.articles')} href={'/app/articles'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconDeviceDesktopAnalytics/>} label={t('navigation.menu.analytics')} href={'/app/analytics'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSettings/>} label={t('navigation.menu.config')} href={'/app/config'}/>
        </div>
      );
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconClock/>} label={t('navigation.menu.timetable')} href={'/app/timetable'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSchool/>} label={t('navigation.menu.students')} href={'/app/students'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconUsers/>} label={t('navigation.menu.courses')} href={'/app/courses'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBriefcase/>} label={t('navigation.menu.teachers')} href={'/app/teachers'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessageDots/>} label={t('navigation.menu.chat')} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={t('navigation.menu.calendar')} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSettings/>} label={t('navigation.menu.config')} href={'/app/organization-config'}/>
        </div>
      );
      break;
    case UserRole.TEACHER:
      menu =
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={t('navigation.menu.assignments')} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconZoomQuestion/>} label={t('navigation.menu.exams')} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={t('navigation.menu.chat')} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={t('navigation.menu.calendar')} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>;
      break;
    case UserRole.STUDENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBook/>} label={t('navigation.menu.subjects')} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStars/>} label={t('navigation.menu.grades')} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHandTwoFingers/>} label={t('navigation.menu.absence')} href={'/app/absence'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={t('navigation.menu.assignments')} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconZoomQuestion/>} label={t('navigation.menu.exams')} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={t('navigation.menu.chat')} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={t('navigation.menu.calendar')} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSpeakerphone/>} label={t('navigation.menu.noticeboard')} href={'/app/noticeboard'}/>
        </div>
      );
      break;
    case UserRole.PARENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={t('navigation.menu.summary')} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStars/>} label={t('navigation.menu.grades')} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHandTwoFingers/>} label={t('navigation.menu.absence')} href={'/app/absence'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={t('navigation.menu.assignments')} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={t('navigation.menu.chat')} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={t('navigation.menu.calendar')} href={'/app/calendar'}/>
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
