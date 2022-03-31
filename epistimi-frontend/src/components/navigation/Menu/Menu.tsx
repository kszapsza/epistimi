import './Menu.scss';
import {
  Briefcase,
  Building,
  Calendar,
  Clock,
  DeviceDesktopAnalytics,
  FileText,
  Home,
  MessageDots,
  Messages,
  School,
  Settings,
  Star,
  Users,
  Writing,
  ZoomQuestion,
} from 'tabler-icons-react';
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
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Building/>} label={'Placówki'} href={'/app/organizations'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Users/>} label={'Użytkownicy'} href={'/app/users'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<FileText/>} label={'Artykuły'} href={'/app/articles'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<DeviceDesktopAnalytics/>} label={'Analityka'} href={'/app/analytics'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Settings/>} label={'Konfiguracja'} href={'/app/config'}/>
        </div>
      );
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Moje przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Clock/>} label={'Planer zajęć'} href={'/app/timetable'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Uczniowie'} href={'/app/students'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Users/>} label={'Klasy'} href={'/app/courses'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Briefcase/>} label={'Personel'} href={'/app/staff'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<MessageDots/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Calendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Settings/>} label={'Konfiguracja'} href={'/app/organization-config'}/>
        </div>
      );
      break;
    case UserRole.TEACHER:
      menu =
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Writing/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<ZoomQuestion/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Messages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Calendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>;
      break;
    case UserRole.STUDENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Star/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Writing/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<ZoomQuestion/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Messages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Calendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>
      );
      break;
    case UserRole.PARENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Star/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Writing/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Messages/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Calendar/>} label={'Kalendarz'} href={'/app/calendar'}/>
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
