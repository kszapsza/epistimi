import React from 'react';
import { MainPage } from './modules/main-page';
import { Navbar, NotFound } from './shared';
import { Summary } from './modules/summary';
import { Route, Routes } from 'react-router-dom';
import './App.scss';

const App = (): JSX.Element => {
  return (
    <>
      <Navbar/>
      <main className={'main'}>
        <Routes>
          <Route path={'/*'} element={<MainPage/>}/>
          <Route path={'/summary'} element={<Summary/>}/>
          <Route path={'/404'} element={<NotFound/>}/>
        </Routes>
      </main>
    </>
  );
}

export { App };
