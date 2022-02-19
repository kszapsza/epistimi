import React from 'react';
import { Footer } from '../../../components/Footer';
import { Outlet } from 'react-router-dom';
import './NavigationFrame.scss';

export const NavigationFrame = (): JSX.Element => {
  return (
    <>
      <div className={'mainframe'}>
        <div>
          <p>Lorem ipsum</p>
          <p>Lorem ipsum</p>
          <p>Lorem ipsum</p>
          <p>Lorem ipsum</p>
          <p>Lorem ipsum</p>
        </div>
        <Outlet/>
      </div>
      <Footer/>
    </>
  );
};
