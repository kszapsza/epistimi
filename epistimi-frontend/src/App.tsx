import './App.scss';
import { ArticleListing, ArticlePage, MainPage } from './modules/main-page';
import { Header, NotFound } from './components';
import { Navigate, Route, Routes } from 'react-router-dom';
import { NavigationFrame } from './modules/navigation/NavigationFrame';
import { Summary } from './modules/summary';
import { useAppSelector } from './store/hooks';

const App = (): JSX.Element => {
  const { isAuthenticated } = useAppSelector((state) => state.auth);

  return (
    <>
      <Header/>
      <main className={'main'}>
        <Routes>
          <Route path={'/'} element={isAuthenticated ? <Navigate to={'/app/summary'}/> : <MainPage/>}>
            <Route path={'/'} element={<ArticleListing/>}/>
            <Route path={'article/:slug'} element={<ArticlePage/>}/>
          </Route>
          <Route path={'/app'} element={isAuthenticated ? <NavigationFrame/> : <Navigate to={'/'}/>}>
            <Route path={'summary'} element={<Summary/>}/>
          </Route>
          <Route path={'/404'} element={<NotFound/>}/>
          <Route path={'/*'} element={<Navigate to={'/404'}/>}/>
        </Routes>
      </main>
    </>
  );
};

export { App };
