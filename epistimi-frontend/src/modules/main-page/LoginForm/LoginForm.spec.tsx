import { LoginForm } from './LoginForm';
import { fireEvent, waitFor } from '@testing-library/react';
import { render } from '../../../utils/test-render';
import React from 'react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('LoginForm component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render form properly', async () => {
    const { getByLabelText } = render(<LoginForm/>);

    await waitFor(() => {
      expect(getByLabelText(/nazwa użytkownika/i)).toHaveValue('');
      expect(getByLabelText(/hasło/i)).toHaveValue('');
    });
  });

  it('should not show any error message before first submit', async () => {
    const { queryByText, getByLabelText } = render(<LoginForm/>);
    const usernameInput = getByLabelText(/nazwa użytkownika/i);

    fireEvent.change(usernameInput, { target: { value: 'abc' } });

    await waitFor(() => {
      expect(usernameInput).toHaveValue('abc');
      expect(queryByText(/niepoprawne dane logowania/i)).toBeNull();
    });
  });

  it('should not show any error message on submit if form is valid', async () => {
    const { getByLabelText, queryByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(/nazwa użytkownika/i);
    const passwordInput = getByLabelText(/hasło/i);

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.change(passwordInput, { target: { value: '123' } });

    await waitFor(() => {
      expect(queryByText(/niepoprawne dane logowania/i)).toBeNull();
    });
  });

  it('should show an error message on submit if password is missing', async () => {
    const { getByLabelText, getByRole, getByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(/nazwa użytkownika/i);
    const submitButton = getByRole('button');

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByText(/niepoprawne dane logowania/i)).toBeInTheDocument();
    });
  });

  it('should show an error message on submit if username is missing', async () => {
    const { getByLabelText, getByRole, getByText } = render(<LoginForm/>);

    const passwordInput = getByLabelText(/hasło/i);
    const submitButton = getByRole('button');

    fireEvent.change(passwordInput, { target: { value: '123' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(getByText(/niepoprawne dane logowania/i)).toBeInTheDocument();
    });
  });

  it('should hide an error message after form was corrected', async () => {
    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(/nazwa użytkownika/i);
    const passwordInput = getByLabelText(/hasło/i);
    const submitButton = getByRole('button');

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(usernameInput).toHaveValue('abc');
      expect(queryByText(/niepoprawne dane logowania/i)).toBeInTheDocument();
    });

    fireEvent.change(passwordInput, { target: { value: '123' } });

    await waitFor(() => {
      expect(queryByText(/niepoprawne dane logowania/i)).toBeNull();
    });
  });

  it.each([
    ['server rejects credentials with (HTTP 401)', { response: { status: 401 } }, /niepoprawne dane logowania/i],
    ['server fails to respond (HTTP 500)', { response: { status: 500 } }, /nie udało się połączyć z serwerem/i],
    ['server fails to respond (no status code)', {}, /nie udało się połączyć z serwerem/i],
  ])('should render an error message if form is valid, but %s', async (testCase, axiosResponse, message) => {
    axiosMock.post.mockRejectedValue(axiosResponse);

    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    fireEvent.change(getByLabelText(/nazwa użytkownika/i), { target: { value: 'abc' } });
    fireEvent.change(getByLabelText(/hasło/i), { target: { value: '123' } });
    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(message)).toBeInTheDocument();
    });
  });

  it('should not render an error message if server accepts login', async () => {
    axiosMock.post.mockResolvedValue({
      data: {
        token: 'some_token',
        firstName: 'Adam',
        lastName: 'Nowak',
        role: 'STUDENT',
        username: 'a.nowak94',
      },
      status: 200,
    });

    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    fireEvent.change(getByLabelText(/nazwa użytkownika/i), { target: { value: 'abc' } });
    fireEvent.change(getByLabelText(/hasło/i), { target: { value: '123' } });
    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(/niepoprawne dane logowania/i)).toBeNull();
    });
  });
});
