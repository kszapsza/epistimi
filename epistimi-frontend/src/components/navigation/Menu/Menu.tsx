import './Menu.scss';
import {
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
  IconStar,
  IconUsers,
  IconWriting,
  IconZoomQuestion,
} from '@tabler/icons';
import { MenuItem } from '../MenuItem';
import { MenuUser } from '../MenuUser';
import { Navbar, ScrollArea } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface MenuProps {
  onMenuItemClick: () => void;
}

export const Menu = (props: MenuProps): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  let menu: JSX.Element;

  switch (user?.role) {
    case UserRole.EPISTIMI_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBuilding/>} label={'Placówki'} href={'/app/organizations'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconUsers/>} label={'Użytkownicy'} href={'/app/users'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconFileText/>} label={'Artykuły'} href={'/app/articles'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconDeviceDesktopAnalytics/>} label={'Analityka'} href={'/app/analytics'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSettings/>} label={'Konfiguracja'} href={'/app/config'}/>
        </div>
      );
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSchool/>} label={'Moje przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconClock/>} label={'Planer zajęć'} href={'/app/timetable'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSchool/>} label={'Uczniowie'} href={'/app/students'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconUsers/>} label={'Klasy'} href={'/app/courses'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconBriefcase/>} label={'Personel'} href={'/app/staff'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessageDots/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSettings/>} label={'Konfiguracja'} href={'/app/organization-config'}/>
        </div>
      );
      break;
    case UserRole.TEACHER:
      menu =
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSchool/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconZoomQuestion/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>;
      break;
    case UserRole.STUDENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconSchool/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStar/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHandTwoFingers/>} label={'Absencja'} href={'/app/absence'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconZoomQuestion/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>
      );
      break;
    case UserRole.PARENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHome/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconStar/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconHandTwoFingers/>} label={'Absencja'} href={'/app/absence'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconWriting/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconMessages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<IconCalendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
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
