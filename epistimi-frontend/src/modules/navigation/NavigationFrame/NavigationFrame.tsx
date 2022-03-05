import './NavigationFrame.scss';
import { Footer } from '../../../components/Footer';
import { Outlet } from 'react-router-dom';

export const NavigationFrame = (): JSX.Element => {
  return (
    <>
      <div className={'navigation-frame'}>
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
