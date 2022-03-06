import './App.scss';
import { ArticleListing, ArticlePage, MainPage } from './modules/main-page';
import { Header, NotFound } from './components';
import { Menu } from './modules/menu';
import { Navigate, Route, Routes } from 'react-router-dom';
import { Organizations } from './modules/organizations';
import { RequireAuth } from './router/RequireAuth/RequireAuth';
import { Summary } from './modules/summary';
import { useAppSelector } from './store/hooks';
import { UserRole } from './dto/user';

const App = (): JSX.Element => {
  const auth = useAppSelector((state) => state.auth);

  return (
    <>
      <Header/>
      <main className={'main'}>
        <Routes>
          <Route path={'/'} element={auth.isAuthenticated ? <Navigate to={'/app/summary'}/> : <MainPage/>}>
            <Route path={'/'} element={<ArticleListing/>}/>
            <Route path={'article/:slug'} element={<ArticlePage/>}/>
          </Route>
          <Route path={'/app'} element={<RequireAuth element={<Menu/>} auth={auth}/>}>
            <Route path={'summary'} element={<Summary/>}/>
            <Route path={'organizations'}
                   element={<RequireAuth element={<Organizations/>} auth={auth} allowedRoles={[UserRole.EPISTIMI_ADMIN]}/>}/>
          </Route>
          <Route path={'/404'} element={<NotFound/>}/>
          <Route path={'/*'} element={<Navigate to={'/404'}/>}/>
        </Routes>
      </main>
    </>
  );
};

export { App };
