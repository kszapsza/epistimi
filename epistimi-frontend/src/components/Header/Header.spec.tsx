import { combineReducers, createStore, Store } from '@reduxjs/toolkit';
import { currentUserReducer, TOKEN_KEY } from '../../store/slices/authSlice';
import { Header } from './Header';
import { render } from '../../utils/test-render';
import { UserRole } from '../../dto/user';
import { waitFor } from '@testing-library/react';

describe('Header component', () => {
  const storeMock: Store = createStore(
    combineReducers({
      auth: currentUserReducer,
    }),
    {
      auth: {
        user: {
          id: '123',
          firstName: 'Jan',
          lastName: 'Kowalski',
          role: UserRole.STUDENT,
          username: 'j.kowalski',
        },
        isAuthenticated: true,
        isFetching: false,
      },
    },
  );

  it('should logout user on button click', async () => {
    const { getByLabelText } = render(<Header/>, storeMock);
    localStorage.setItem(TOKEN_KEY, 'foo');

    getByLabelText('Log out').click();

    await waitFor(() => {
      expect(localStorage.getItem(TOKEN_KEY)).toBeFalsy();
      expect(storeMock.getState().auth.isAuthenticated).toBeFalsy();
      expect(storeMock.getState().auth.isFetching).toBeFalsy();
      expect(storeMock.getState().auth.user).toBeNull();
    });
  });
});
