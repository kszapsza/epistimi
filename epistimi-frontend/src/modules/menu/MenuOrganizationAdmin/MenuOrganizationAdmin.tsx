import { AccessTime, Chat, DateRange, Home, School, Settings, Work } from '@mui/icons-material';
import { MenuItem } from '../MenuItem';

export const MenuOrganizationAdmin = (): JSX.Element => {
  return (
    <>
      <MenuItem icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
      <MenuItem icon={<School/>} label={'Moje przedmioty'} href={'/app/subjects'}/>
      <MenuItem icon={<AccessTime/>} label={'Planer zajęć'} href={'/app/timetable'}/>
      <MenuItem icon={<School/>} label={'Uczniowie i klasy'} href={'/app/students'}/>
      <MenuItem icon={<Work/>} label={'Personel'} href={'/app/staff'}/>
      <MenuItem icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
      <MenuItem icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
      <MenuItem icon={<Settings/>} label={'Konfiguracja'} href={'/app/organization-config'}/>
    </>
  );
};
