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

  const SETTINGS_REGEXP = /navigation\.menuUser\.settings/;
  const LOG_OUT_REGEXP = /navigation\.menuUser\.logOut/;

  it('should open menu on button click', async () => {
    const storeMock = getStoreMock();
    const { getByRole, queryByText } = render(<MenuUser/>, storeMock);

    expect(queryByText(SETTINGS_REGEXP)).not.toBeInTheDocument();
    expect(queryByText(LOG_OUT_REGEXP)).not.toBeInTheDocument();

    getByRole('button').click();

    await waitFor(() => {
      expect(queryByText(SETTINGS_REGEXP)).toBeInTheDocument();
      expect(queryByText(LOG_OUT_REGEXP)).toBeInTheDocument();
    });
  });


  it('should log out user on logout option click', async () => {
    const storeMock = getStoreMock();
    const { getByRole, getByText } = render(<MenuUser/>, storeMock);

    localStorage.setItem(TOKEN_KEY, 'foo');

    getByRole('button').click();

    await waitFor(() => {
      getByText(LOG_OUT_REGEXP).click();
      expect(localStorage.getItem(TOKEN_KEY)).toBeFalsy();
      expect(storeMock.getState().auth.isAuthenticated).toBeFalsy();
      expect(storeMock.getState().auth.isFetching).toBeFalsy();
      expect(storeMock.getState().auth.user).toBeNull();
    });
  });
});
