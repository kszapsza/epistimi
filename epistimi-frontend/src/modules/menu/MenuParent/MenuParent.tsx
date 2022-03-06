import { Assignment, Chat, DateRange, Grade, Home } from '@mui/icons-material';
import { MenuItem } from '../MenuItem';

export const MenuParent = (): JSX.Element => {
  return (
    <>
      <MenuItem icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
      <MenuItem icon={<Grade/>} label={'Oceny'} href={'/app/grades'}/>
      <MenuItem icon={<Assignment/>} label={'Zadania domowe'} href={'/app/assignments'}/>
      <MenuItem icon={<Chat/>} label={'Wiadomości'} href={'/app/chat'}/>
      <MenuItem icon={<DateRange/>} label={'Kalendarz'} href={'/app/calendar'}/>
    </>
  );
};
