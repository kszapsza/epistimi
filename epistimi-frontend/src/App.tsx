import React from 'react';
import './App.scss';
import { MainPage } from './modules/main-page';
import { Navbar } from './modules/shared';

const App = (): JSX.Element => {
  return (
    <main className="main">
      <Navbar/>
      <MainPage/>
    </main>
  );
}

export { App };
