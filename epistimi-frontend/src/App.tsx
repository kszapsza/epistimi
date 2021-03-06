import { ArticleListing, ArticlePage, MainPage } from './components/main-page';
import { CourseDetails, CoursesListing } from './components/courses';
import { Navigate, useRoutes } from 'react-router-dom';
import { NotFound, Shell } from './components/navigation';
import { NotImplementedYet } from './components/common';
import { OrganizationDetails, OrganizationsListing } from './components/organizations';
import { RequireAuth } from './router/RequireAuth';
import { Summary } from './components/summary';
import { TeacherDetails } from './components/teachers/TeacherDetails';
import { TeachersListing } from './components/teachers';
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
        {
          path: 'courses',
          element: (
            <RequireAuth
              element={<CoursesListing/>}
              auth={auth}
              allowedRoles={[UserRole.ORGANIZATION_ADMIN]}
            />
          ),
        },
        {
          path: 'courses/:id',
          element: (
            <RequireAuth
              element={<CourseDetails/>}
              auth={auth}
              allowedRoles={[UserRole.ORGANIZATION_ADMIN]}
            />
          ),
        },
        {
          path: 'students/:id',
          element: (
            <RequireAuth
              element={<NotImplementedYet/>}
              auth={auth}
              allowedRoles={[UserRole.ORGANIZATION_ADMIN]}
            />
          ),
        },
        {
          path: 'teachers',
          element: (
            <RequireAuth
              element={<TeachersListing/>}
              auth={auth}
              allowedRoles={[UserRole.ORGANIZATION_ADMIN]}
            />
          ),
        },
        {
          path: 'teachers/:id',
          element: (
            <RequireAuth
              element={<TeacherDetails/>}
              auth={auth}
              allowedRoles={[UserRole.ORGANIZATION_ADMIN]}
            />
          ),
        },
        {
          path: 'subjects',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'timetable',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'students',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'chat',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'calendar',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'organization-config',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'users',
          element: (
            <RequireAuth
              element={<NotImplementedYet/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
        {
          path: 'articles',
          element: (
            <RequireAuth
              element={<NotImplementedYet/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
        {
          path: 'analytics',
          element: (
            <RequireAuth
              element={<NotImplementedYet/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
        {
          path: 'config',
          element: (
            <RequireAuth
              element={<NotImplementedYet/>}
              auth={auth}
              allowedRoles={[UserRole.EPISTIMI_ADMIN]}
            />
          ),
        },
        {
          path: 'grades',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'absence',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'assignments',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'exams',
          element: (
            <NotImplementedYet/>
          ),
        },
        {
          path: 'noticeboard',
          element: (
            <NotImplementedYet/>
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
