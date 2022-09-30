import { fireEvent, waitFor } from '@testing-library/react';
import { LoginForm } from './LoginForm';
import { render } from '../../../utils/test-render';
import { store } from '../../../store/config';
import { TOKEN_KEY } from '../../../store/slices/authSlice';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('LoginForm component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  const USERNAME_LABEL_REGEXP = /mainPage\.loginForm\.username/;
  const PASSWORD_LABEL_REGEXP = /mainPage\.loginForm\.password/;
  const INCORRECT_CREDENTIALS_REGEXP = /mainPage\.loginForm\.incorrectCredentials/;
  const CONNECTION_FAILED_REGEXP = /mainPage\.loginForm\.connectionFailed/;

  it('should render form properly', async () => {
    const { getByLabelText } = render(<LoginForm/>);

    await waitFor(() => {
      expect(getByLabelText(USERNAME_LABEL_REGEXP)).toHaveValue('');
      expect(getByLabelText(PASSWORD_LABEL_REGEXP)).toHaveValue('');
    });
  });

  it('should not show any error message before first submit', async () => {
    const { queryByText, getByLabelText } = render(<LoginForm/>);
    const usernameInput = getByLabelText(USERNAME_LABEL_REGEXP);

    fireEvent.change(usernameInput, { target: { value: 'abc' } });

    await waitFor(() => {
      expect(usernameInput).toHaveValue('abc');
      expect(queryByText(INCORRECT_CREDENTIALS_REGEXP)).toBeNull();
    });
  });

  it('should not show any error message on submit if form is valid', async () => {
    const { getByLabelText, queryByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(USERNAME_LABEL_REGEXP);
    const passwordInput = getByLabelText(PASSWORD_LABEL_REGEXP);

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.change(passwordInput, { target: { value: '123' } });

    await waitFor(() => {
      const incorrectCredentialsWarning = queryByText(INCORRECT_CREDENTIALS_REGEXP);
      expect(incorrectCredentialsWarning).toBeNull();
    });
  });

  it('should show an error message on submit if password is missing', async () => {
    const { getByLabelText, getByRole, getByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(USERNAME_LABEL_REGEXP);
    const submitButton = getByRole('button');

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByText(INCORRECT_CREDENTIALS_REGEXP)).toBeInTheDocument();
    });
  });

  it('should show an error message on submit if username is missing', async () => {
    const { getByLabelText, getByRole, getByText } = render(<LoginForm/>);

    const passwordInput = getByLabelText(PASSWORD_LABEL_REGEXP);
    const submitButton = getByRole('button');

    fireEvent.change(passwordInput, { target: { value: '123' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByText(INCORRECT_CREDENTIALS_REGEXP)).toBeInTheDocument();
    });
  });

  it('should hide an error message after form was corrected', async () => {
    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(USERNAME_LABEL_REGEXP);
    const passwordInput = getByLabelText(PASSWORD_LABEL_REGEXP);
    const submitButton = getByRole('button');

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(usernameInput).toHaveValue('abc');
      expect(queryByText(INCORRECT_CREDENTIALS_REGEXP)).toBeInTheDocument();
    });

    fireEvent.change(passwordInput, { target: { value: '123' } });

    await waitFor(() => {
      expect(queryByText(INCORRECT_CREDENTIALS_REGEXP)).toBeNull();
    });
  });

  it.each([
    ['server rejects credentials with (HTTP 401)', { response: { status: 401 } }, INCORRECT_CREDENTIALS_REGEXP],
    ['server fails to respond (HTTP 500)', { response: { status: 500 } }, CONNECTION_FAILED_REGEXP],
    ['server fails to respond (no status code)', {}, CONNECTION_FAILED_REGEXP],
  ])('should render an error message if form is valid, but %s', async (testCase, axiosResponse, message) => {
    axiosMock.post.mockRejectedValue(axiosResponse);

    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    fireEvent.change(getByLabelText(USERNAME_LABEL_REGEXP), { target: { value: 'abc' } });
    fireEvent.change(getByLabelText(PASSWORD_LABEL_REGEXP), { target: { value: '123' } });
    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(message)).toBeInTheDocument();
    });
  });

  it('should not render an error message if server accepts login', async () => {
    axiosMock.post.mockResolvedValue({
      data: {
        token: 'some_token',
      },
    });

    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    fireEvent.change(getByLabelText(USERNAME_LABEL_REGEXP), { target: { value: 'abc' } });
    fireEvent.change(getByLabelText(PASSWORD_LABEL_REGEXP), { target: { value: '123' } });
    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(INCORRECT_CREDENTIALS_REGEXP)).toBeNull();
    });
  });

  it('should log in and store user data', async () => {
    const userData = {
      firstName: 'Adam',
      lastName: 'Nowak',
      role: 'STUDENT',
      username: 'a.nowak94',
    };

    const loginMock = axiosMock.post.mockResolvedValue({
      data: {
        token: 'some_token',
      },
    });
    const currentUserMock = axiosMock.get.mockResolvedValue({
      data: {
        ...userData,
      },
    });

    const { getByLabelText, getByRole } = render(<LoginForm/>, store);

    fireEvent.change(getByLabelText(USERNAME_LABEL_REGEXP), { target: { value: 'abc' } });
    fireEvent.change(getByLabelText(PASSWORD_LABEL_REGEXP), { target: { value: '123' } });
    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(loginMock).toHaveBeenCalledTimes(1);
      expect(currentUserMock).toHaveBeenCalledTimes(1);
      expect(localStorage.getItem(TOKEN_KEY)).toBe('some_token');
      expect(store.getState().auth.isAuthenticated).toBeTruthy();
      expect(store.getState().auth.isFetching).toBeFalsy();
      expect(store.getState().auth.user).toStrictEqual(userData);
    });
  });
});
