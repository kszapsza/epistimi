import { combineReducers, createStore, Store } from '@reduxjs/toolkit';
import { currentUserReducer, TOKEN_KEY } from '../../../store/slices/authSlice';
import { MenuUser } from './MenuUser';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';
import { waitFor } from '@testing-library/react';

describe('MenuUser component', () => {
  const getStoreMock = (
    role: UserRole = UserRole.STUDENT,
  ): Store => createStore(
    combineReducers({
      auth: currentUserReducer,
    }),
    {
      auth: {
        user: {
          id: '123',
          firstName: 'Jan',
          lastName: 'Kowalski',
          role,
          username: 'j.kowalski',
        },
        isAuthenticated: true,
        isFetching: false,
      },
    },
  );

  it('should open menu on button click', async () => {
    const storeMock = getStoreMock();
    const { getByRole, queryByText } = render(<MenuUser/>, storeMock);

    expect(queryByText(/ustawienia/i)).not.toBeInTheDocument();
    expect(queryByText(/wyloguj się/i)).not.toBeInTheDocument();

    getByRole('button').click();

    await waitFor(() => {
      expect(queryByText(/ustawienia/i)).toBeInTheDocument();
      expect(queryByText(/wyloguj się/i)).toBeInTheDocument();
    });
  });

  it.each([
    UserRole.EPISTIMI_ADMIN,
    UserRole.ORGANIZATION_ADMIN,
    UserRole.TEACHER,
    UserRole.STUDENT,
  ])('should not show child selector when user role is not PARENT', async (role) => {
    const storeMock = getStoreMock(role);
    const { getByRole, queryByText } = render(<MenuUser/>, storeMock);

    getByRole('button').click();

    await waitFor(() => {
      expect(queryByText(/wybierz ucznia/i)).not.toBeInTheDocument();
    });
  });

  it('should show child selector when user role is PARENT', async () => {
    const storeMock = getStoreMock(UserRole.PARENT);
    const { getByRole, queryByText } = render(<MenuUser/>, storeMock);

    getByRole('button').click();

    await waitFor(() => {
      expect(queryByText(/wybierz ucznia/i)).toBeInTheDocument();
    });
  });

  it('should log out user on logout option click', async () => {
    const storeMock = getStoreMock();
    const { getByRole, getByText } = render(<MenuUser/>, storeMock);

    localStorage.setItem(TOKEN_KEY, 'foo');

    getByRole('button').click();

    await waitFor(() => {
      getByText(/wyloguj się/i).click();
      expect(localStorage.getItem(TOKEN_KEY)).toBeFalsy();
      expect(storeMock.getState().auth.isAuthenticated).toBeFalsy();
      expect(storeMock.getState().auth.isFetching).toBeFalsy();
      expect(storeMock.getState().auth.user).toBeNull();
    });
  });
});
