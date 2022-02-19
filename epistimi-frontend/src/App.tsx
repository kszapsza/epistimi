import React from 'react';
import { Article, ArticleListing, MainPage } from './modules/main-page';
import { Header, NotFound } from './components';
import { Navigate, Route, Routes } from 'react-router-dom';
import { NavigationFrame } from './modules/navigation/NavigationFrame';
import { Summary } from './modules/summary';
import './App.scss';

const App = (): JSX.Element => {
  return (
    <>
      <Header/>
      <main className={'main'}>
        <Routes>
          <Route path={'/'} element={<MainPage/>}>
            <Route path={'/'} element={<ArticleListing/>}/>
            <Route path={'article/*'} element={<Article/>}/>
          </Route>
          <Route path={'/app'} element={<NavigationFrame/>}>
            <Route path={'summary'} element={<Summary/>}/>
          </Route>
          <Route path={'/404'} element={<NotFound/>}/>
          <Route path={'/*'} element={<Navigate to={'/404'}/>}/>
        </Routes>
      </main>
    </>
  );
}

export { App };
