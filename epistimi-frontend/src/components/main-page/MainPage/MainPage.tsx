import './MainPage.scss';
import { AppShell, Header } from '@mantine/core';
import { Header as EpistimiHeader, Footer } from '../../navigation';
import { HashLink } from 'react-router-hash-link';
import { LoginForm } from '../LoginForm';
import { Outlet } from 'react-router-dom';
import { useDocumentTitle } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const MainPage = (): JSX.Element => {
  const { t } = useTranslation();

  useDocumentTitle();

  const header =
    <Header
      className={'app-shell-header'}
      height={45}
      p={'lg'}
    >
      <EpistimiHeader/>
    </Header>;

  return (
    <AppShell
      fixed
      header={header}
      navbarOffsetBreakpoint={'sm'}
      padding={'xl'}
    >
      <div className={'main-page'}>
        <div className={'main-page-left'}>
          <Outlet/>
        </div>
        <div className={'main-page-right'}>
          <div className={'main-page-jump-top'}>
            <HashLink smooth to={'#'}>
              Wróć do góry
            </HashLink>
          </div>
          <div className={'main-page-copy'}>
            <h2>{t('mainPage.welcomeToEpistimi')}</h2>
            <div className={'main-page-copy-desc'}>
              {t('mainPage.promo')}
            </div>
          </div>
          <LoginForm/>
          <Footer/>
        </div>
      </div>
    </AppShell>
  );
};
