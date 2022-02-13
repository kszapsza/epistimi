import React from 'react';
import { MainPage } from './modules/main-page';
import { Navbar, NotFound } from './modules/shared';
import { Summary } from './modules/summary';
import { Navigate, Route, Routes } from 'react-router-dom';
import './App.scss';

const App = (): JSX.Element => {
  return (
    <>
      <Navbar/>
      <main className={'main'}>
        <Routes>
          <Route path={'/'} element={<MainPage/>}/>
          <Route path={'/summary'} element={<Summary/>}/>
          <Route path={'/404'} element={<NotFound/>}/>
          <Route path={'*'} element={<Navigate to={'/404'}/>}/>
        </Routes>
      </main>
    </>
  );
}

export { App };
