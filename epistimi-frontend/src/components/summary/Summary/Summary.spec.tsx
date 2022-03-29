import { combineReducers, createStore, Store } from '@reduxjs/toolkit';
import { currentUserReducer } from '../../../store/slices/authSlice';
import { render } from '../../../utils/test-render';
import { Summary } from './Summary';
import { UserRole } from '../../../dto/user';

describe('Summary component', () => {
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

  it('should render user greeting', () => {
    const { getByRole } = render(<Summary/>, storeMock);
    expect(getByRole('heading')).toHaveTextContent(/witaj, jan/i);
  });
});
