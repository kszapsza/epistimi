import { combineReducers, createStore, Store } from '@reduxjs/toolkit';
import { currentUserReducer, TOKEN_KEY } from '../../../store/slices/authSlice';
import { Header } from './Header';
import { render } from '../../../utils/test-render';
import { UserRole } from '../../../dto/user';
import { waitFor } from '@testing-library/react';

describe('Header component', () => {
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

  it('should render title', () => {
    const storeMock = getStoreMock();
    const { getByText } = render(<Header/>, storeMock);

    expect(getByText('Epistimi')).toBeInTheDocument();
  });

  it('should render admin title', () => {
    const storeMock = getStoreMock(UserRole.EPISTIMI_ADMIN);
    const { getByText } = render(<Header/>, storeMock);

    expect(getByText('Epistimi')).toBeInTheDocument();
    expect(getByText('admin')).toBeInTheDocument();
  });

  it('should logout user on button click', async () => {
    const storeMock = getStoreMock();
    const { getByLabelText } = render(
      <Header onBurgerClick={jest.fn()} navbarOpened={true}/>,
      storeMock);
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
