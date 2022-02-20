import React from 'react';
import { LoginForm } from '../LoginForm';
import { Footer } from '../../../components/Footer';
import { Outlet } from 'react-router-dom';
import './MainPage.scss';
import { HashLink } from 'react-router-hash-link';

export const MainPage = (): JSX.Element => {
  return (
    <div className={'main-page'}>
      <div className={'main-page-left'}>
        <Outlet/>
      </div>
      <div className={'main-page-right'}>
        <HashLink smooth to={'#'}>
          <div className={'main-page-jump-top'}>
            Wróć do góry
          </div>
        </HashLink>
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
}
