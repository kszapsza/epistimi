import React from 'react';
import './App.scss';
import { MainPage } from './modules/main-page';
import { Navbar } from './modules/shared';

const App = (): JSX.Element => {
  return (
    <div className={'main'}>
      <Navbar/>
      <MainPage/>
    </div>
  );
}

export default App;
