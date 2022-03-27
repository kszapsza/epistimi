import './Menu.scss';
import {
  AccessTime,
  Assignment,
  Chat,
  CorporateFare,
  DateRange,
  Feed,
  Grade,
  Home,
  Insights,
  People,
  Quiz,
  School,
  Settings,
  Work,
} from '@mui/icons-material';
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
          <MenuItem onClick={props.onMenuItemClick} icon={<CorporateFare/>} label={'Placówki'} href={'/app/organizations'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<People/>} label={'Użytkownicy'} href={'/app/users'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Feed/>} label={'Artykuły'} href={'/app/articles'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Insights/>} label={'Analityka'} href={'/app/analytics'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Settings/>} label={'Konfiguracja'} href={'/app/config'}/>
        </div>
      );
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Moje przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<AccessTime/>} label={'Planer zajęć'} href={'/app/timetable'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Uczniowie i klasy'} href={'/app/students'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Work/>} label={'Personel'} href={'/app/staff'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Settings/>} label={'Konfiguracja'} href={'/app/organization-config'}/>
        </div>
      );
      break;
    case UserRole.TEACHER:
      menu =
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Assignment/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Quiz/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>;
      break;
    case UserRole.STUDENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<School/>} label={'Przedmioty'} href={'/app/subjects'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Grade/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Assignment/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Quiz/>} label={'Sprawdziany'} href={'/app/exams'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>
      );
      break;
    case UserRole.PARENT:
      menu = (
        <div>
          <MenuItem onClick={props.onMenuItemClick} icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Grade/>} label={'Oceny'} href={'/app/grades'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Assignment/>} label={'Zadania domowe'} href={'/app/assignments'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
          <MenuItem onClick={props.onMenuItemClick} icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
        </div>
      );
      break;
    default:
      menu = <></>;
  }

  return (
    <div className={'menu'}>
      {user?.role === UserRole.PARENT &&
        <Navbar.Section>
          {/*TODO <MenuSelectStudent/>*/}
        </Navbar.Section>}

      {user?.role === UserRole.TEACHER &&
        <Navbar.Section>
          {/*TODO <MenuSelectOrganization/>*/}
        </Navbar.Section>}

      <Navbar.Section grow component={ScrollArea} mx="-xs" px="xs">
        {menu}
      </Navbar.Section>

      <Navbar.Section>
        <MenuUser/>
      </Navbar.Section>
    </div>
  );
};
