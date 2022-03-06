import { Assignment, Chat, DateRange, Home, Quiz, School } from '@mui/icons-material';
import { MenuItem } from '../MenuItem';

export const MenuTeacher = (): JSX.Element => {
  return (
    <>
      <MenuItem icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
      <MenuItem icon={<School/>} label={'Przedmioty'} href={'/app/subjects'}/>
      <MenuItem icon={<Assignment/>} label={'Zadania domowe'} href={'/app/assignments'}/>
      <MenuItem icon={<Quiz/>} label={'Sprawdziany'} href={'/app/exams'}/>
      <MenuItem icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
      <MenuItem icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
    </>
  );
};
