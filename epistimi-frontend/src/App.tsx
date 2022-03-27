import { ArticleListing, ArticlePage, MainPage } from './components/main-page';
import { Navigate, Route, Routes } from 'react-router-dom';
import { NotFound, Shell } from './components/navigation';
import { OrganizationDetails, OrganizationsListing } from './components/organizations';
import { RequireAuth } from './router/RequireAuth/RequireAuth';
import { Summary } from './components/summary';
import { useAppSelector } from './store/hooks';
import { UserRole } from './dto/user';

const App = (): JSX.Element => {
  const auth = useAppSelector((state) => state.auth);

  return (
    <>
      <Routes>
        <Route path={'/'} element={auth.isAuthenticated ? <Navigate to={'/app/summary'}/> : <MainPage/>}>
          <Route path={'/'} element={<ArticleListing/>}/>
          <Route path={'article/:slug'} element={<ArticlePage/>}/>
        </Route>
        <Route path={'/app'} element={<RequireAuth element={<Shell/>} auth={auth}/>}>
          <Route path={'summary'} element={<Summary/>}/>
          <Route path={'organizations'}
                 element={<RequireAuth element={<OrganizationsListing/>} auth={auth} allowedRoles={[UserRole.EPISTIMI_ADMIN]}/>}/>
          <Route path={'organizations/:id'}
                 element={<RequireAuth element={<OrganizationDetails/>} auth={auth} allowedRoles={[UserRole.EPISTIMI_ADMIN]}/>}/>
        </Route>
        <Route path={'/404'} element={<NotFound/>}/>
        <Route path={'/*'} element={<Navigate to={'/404'}/>}/>
      </Routes>
    </>
  );
};

export { App };
