import { Article } from '../Article';
import { ArticleListing } from '../ArticleListing';
import { LoginForm } from '../LoginForm';
import { Footer } from '../../../shared/Footer';
import { Navigate, Route, Routes } from 'react-router-dom';
import React from 'react';
import './MainPage.scss';

export const MainPage = (): JSX.Element => {
  return (
    <div className="main-page">
      <div className="main-page-left">
        <Routes>
          <Route path={'/'} element={<ArticleListing/>}/>
          <Route path={'/article/*'} element={<Article/>}/>
          <Route path={'/*'} element={<Navigate to={'/404'}/>}/>
        </Routes>
      </div>
      <div className="main-page-right">
        <div className="main-page-copy">
          <h2>E&#x2011;dziennik i&nbsp;platforma e&#x2011;learningowa – od teraz w&nbsp;jednym miejscu.</h2>
          <div className="main-page-copy-desc">
            Zintegrowany system wspomagający dydaktykę, także w&nbsp;kształceniu zdalnym.
            Zaprojektowany z&nbsp;myślą o&nbsp;uczniach, rodzicach i&nbsp;nauczycielach.
          </div>
        </div>
        <LoginForm/>
        <Footer/>
      </div>
    </div>
  )
}
