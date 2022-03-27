import './Shell.scss';
import { AppShell, Header, Navbar } from '@mantine/core';
import { Header as EpistimiHeader } from '../Header';
import { Menu } from '../Menu';
import { Outlet } from 'react-router';
import { useDisclosure } from '@mantine/hooks';

export const Shell = (): JSX.Element => {
  const [navbarOpened, navbarHandlers] = useDisclosure(false);

  const header =
    <Header
      className={'app-shell-header'}
      height={45}
      p={'lg'}
    >
      <EpistimiHeader
        navbarOpened={navbarOpened}
        onBurgerClick={navbarHandlers.toggle}
      />
    </Header>;

  const navbar =
    <Navbar
      className={'app-shell-navbar'}
      hidden={!navbarOpened}
      hiddenBreakpoint={'sm'}
      p={'xs'}
      width={{ sm: 250 }}
    >
      <Menu onMenuItemClick={navbarHandlers.close}/>
    </Navbar>;

  return (
    <AppShell
      className={'app-shell-outlet'}
      fixed
      header={header}
      navbar={navbar}
      navbarOffsetBreakpoint={'sm'}
      padding={'xl'}
    >
      <Outlet/>
    </AppShell>
  );
};
