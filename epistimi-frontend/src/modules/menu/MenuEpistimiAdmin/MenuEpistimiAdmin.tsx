import { CorporateFare, Feed, Home, Insights, People, Settings } from '@mui/icons-material';
import { MenuItem } from '../MenuItem';

export const MenuEpistimiAdmin = (): JSX.Element => {
  return (
    <>
      <MenuItem icon={<Home/>} label={'Strona główna'} href={'/app/summary'}/>
      <MenuItem icon={<CorporateFare/>} label={'Placówki'} href={'/app/organizations'}/>
      <MenuItem icon={<People/>} label={'Użytkownicy'} href={'/app/users'}/>
      <MenuItem icon={<Feed/>} label={'Artykuły'} href={'/app/articles'}/>
      <MenuItem icon={<Insights/>} label={'Analityka'} href={'/app/analytics'}/>
      <MenuItem icon={<Settings/>} label={'Konfiguracja'} href={'/app/config'}/>
    </>
  );
};
