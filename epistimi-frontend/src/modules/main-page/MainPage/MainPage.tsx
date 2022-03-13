import './MainPage.scss';
import { Footer } from '../../../components/Footer';
import { HashLink } from 'react-router-hash-link';
import { LoginForm } from '../LoginForm';
import { Outlet } from 'react-router-dom';
import { useEffect } from 'react';

export const MainPage = (): JSX.Element => {
  useEffect(() => {
    document.title = 'Epistimi';
  }, []);

  return (
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
          <h2>E&#x2011;dziennik i&nbsp;platforma e&#x2011;learningowa – od teraz w&nbsp;jednym miejscu.</h2>
          <div className={'main-page-copy-desc'}>
            Zintegrowany system wspomagający dydaktykę, także w&nbsp;kształceniu zdalnym.
            Zaprojektowany z&nbsp;myślą o&nbsp;uczniach, rodzicach i&nbsp;nauczycielach.
          </div>
        </div>
        <LoginForm/>
        <Footer/>
      </div>
    </div>
  );
};
