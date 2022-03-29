import { ArticleListing, ArticlePage, MainPage } from './components/main-page';
import { Navigate, useRoutes } from 'react-router-dom';
import { NotFound, Shell } from './components/navigation';
import { OrganizationDetails, OrganizationsListing } from './components/organizations';
import { RequireAuth } from './router/RequireAuth';
import { Summary } from './components/summary';
import { useAppSelector } from './store/hooks';
import { UserRole } from './dto/user';

const App = (): JSX.Element => {
  const auth = useAppSelector((state) => state.auth);

  const routes = useRoutes([
    {
      path: '/',
      element: auth.isAuthenticated ? <Navigate to={'/app/summary'}/> : <MainPage/>,
      children: [
        {
          path: '/',
          element: <ArticleListing/>,
        },
        {
          path: 'article/:slug',
          element: <ArticlePage/>,
        },
      ],
    },
    {
      path: '/app',
      element: (
        <RequireAuth
          element={<Shell/>}
          auth={auth}
        />
      ),
      children: [
        {
          path: 'summary',
          element: <Summary/>,
        },
        {
          path: 'organizations',
          element: (
            <RequireAuth
              element={<OrganizationsListing/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
        {
          path: 'organizations/:id',
          element: (
            <RequireAuth
              element={<OrganizationDetails/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
      ],
    },
    {
      path: '/404',
      element: <NotFound/>,
    },
    {
      path: '/*',
      element: <Navigate to={'/404'}/>,
    },
  ]);

  return <>{routes}</>;
};

export { App };
