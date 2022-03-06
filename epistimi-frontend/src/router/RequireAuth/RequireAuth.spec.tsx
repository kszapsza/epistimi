import { AuthState } from '../../store/slices/authSlice';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { render, waitFor } from '@testing-library/react';
import { RequireAuth } from './RequireAuth';
import { UserRole } from '../../dto/user';
import React from 'react';

describe('RequireAuth wrapper', () => {
  const prepareRouter = (
    config: {
      redirectElement: JSX.Element,
      protectedElement: JSX.Element,
    },
  ): JSX.Element => {
    return (
      <MemoryRouter initialEntries={['/protected-path']}>
        <Routes>
          <Route path={'/'} element={config.redirectElement}/>
          <Route path={'/protected-path'} element={config.protectedElement}/>
        </Routes>
      </MemoryRouter>
    );
  };

  it('should redirect if user is not authenticated at all', async () => {
    const auth: AuthState = {
      user: null,
      isAuthenticated: false,
      isFetching: false,
    };
    const { queryByText } = render(prepareRouter({
      redirectElement: <p>redirected</p>,
      protectedElement:
        <RequireAuth
          auth={auth}
          element={<p>protected element</p>}
        />,
    }));
    await waitFor(() => {
      expect(queryByText(/protected element/i)).not.toBeInTheDocument();
      expect(queryByText(/redirected/i)).toBeInTheDocument();
    });
  });

  const studentAuth: AuthState = {
    user: {
      id: '123',
      firstName: 'Jan',
      lastName: 'Kowalski',
      role: UserRole.STUDENT,
      username: 'j.kowalski',
    },
    isAuthenticated: true,
    isFetching: false,
  };

  it('should redirect if user is authenticated, but doesn\'t have an allowed role', async () => {
    const { queryByText } = render(prepareRouter({
      redirectElement: <p>redirected</p>,
      protectedElement:
        <RequireAuth
          auth={studentAuth}
          element={<p>protected element</p>}
          allowedRoles={[UserRole.EPISTIMI_ADMIN, UserRole.ORGANIZATION_ADMIN]}
        />,
    }));
    await waitFor(() => {
      expect(queryByText(/protected element/i)).not.toBeInTheDocument();
      expect(queryByText(/redirected/i)).toBeInTheDocument();
    });
  });

  it('should render a component if user is authenticated and allowed roles aren\'t specified', async () => {
    const { queryByText } = render(prepareRouter({
      redirectElement: <p>redirected</p>,
      protectedElement:
        <RequireAuth
          auth={studentAuth}
          element={<p>protected element</p>}
        />,
    }));
    await waitFor(() => {
      expect(queryByText(/protected element/i)).toBeInTheDocument();
      expect(queryByText(/redirected/i)).not.toBeInTheDocument();
    });
  });

  it('should render a component if user is authenticated and has an allowed role', async () => {
    const { queryByText } = render(prepareRouter({
      redirectElement: <p>redirected</p>,
      protectedElement:
        <RequireAuth
          auth={studentAuth}
          element={<p>protected element</p>}
          allowedRoles={[UserRole.STUDENT, UserRole.TEACHER]}
        />,
    }));
    await waitFor(() => {
      expect(queryByText(/protected element/i)).toBeInTheDocument();
      expect(queryByText(/redirected/i)).not.toBeInTheDocument();
    });
  });
});
