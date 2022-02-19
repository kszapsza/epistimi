import React from 'react';
import { fireEvent, getByText, render, waitFor } from '@testing-library/react';
import { LoginForm } from './LoginForm';

describe('LoginForm component', () => {
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
    const { getByLabelText, getByRole, queryByText } = render(<LoginForm/>);

    const usernameInput = getByLabelText(/nazwa użytkownika/i);
    const passwordInput = getByLabelText(/hasło/i);
    const submitButton = getByRole('button');

    fireEvent.change(usernameInput, { target: { value: 'abc' } });
    fireEvent.change(passwordInput, { target: { value: '123' } });
    fireEvent.click(submitButton);

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
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(usernameInput).toHaveValue('abc');
      expect(passwordInput).toHaveValue('123');
      expect(queryByText(/niepoprawne dane logowania/i)).toBeNull();
    });
  });

  // TODO: test for login form integration with backend
});