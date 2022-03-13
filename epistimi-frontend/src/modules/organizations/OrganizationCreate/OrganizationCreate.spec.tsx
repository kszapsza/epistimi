import { fireEvent, waitFor } from '@testing-library/react';
import { OrganizationCreate } from './OrganizationCreate';
import { render } from '../../../utils/test-render';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationCreate component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render form properly', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationAdminsResponse,
    });
    const { getByLabelText, getByRole, queryByText } = render(<OrganizationCreate onCreated={jest.fn()}/>);

    await waitFor(() => {
      const input = getByLabelText(/nazwa/i) as HTMLInputElement;
      const select = getByLabelText(/administrator/i) as HTMLSelectElement;
      const button = getByRole('button') as HTMLButtonElement;

      expect(axiosMock.get).toHaveBeenCalledTimes(1);
      expect(input).toBeInTheDocument();
      expect(select).toBeInTheDocument();
      expect(select.options).toHaveLength(1);
      expect(select.options[0].value).toBe('42');
      expect(select.options[0].innerHTML).toBe('Kowalski Jan (j.kowalski)');
      expect(button).toBeInTheDocument();
      expect(button.innerHTML).toBe('Utwórz');

      expect(queryByText(/wszystkie pola są wymagane/i)).toBeNull();
      expect(queryByText(/błąd serwera/i)).toBeNull();
    });
  });

  it('should show an error message if form is empty', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationAdminsResponse,
    });
    const onCreatedMock = jest.fn();
    const { getByRole, getByText } = render(<OrganizationCreate onCreated={onCreatedMock}/>);

    await waitFor(() => {
      const button = getByRole('button') as HTMLButtonElement;
      fireEvent.click(button);

      expect(getByText(/wszystkie pola są wymagane/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it('should show an error message if server fails to respond', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationAdminsResponse,
    });
    axiosMock.post.mockRejectedValue({});

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, getByText } = render(<OrganizationCreate onCreated={onCreatedMock}/>);

    await waitFor(() => {
      fireEvent.change(getByLabelText(/nazwa/i), { target: { value: 'SP7' } });
      fireEvent.change(getByLabelText(/administrator/i), { target: { value: '42' } });
      fireEvent.click(getByRole('button'));

      expect(getByText(/błąd serwera/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it('should call `onCreated` callback on successful organization creation', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationAdminsResponse,
    });
    axiosMock.post.mockResolvedValue({
      data: newOrganizationResponse,
    });

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, queryByText } = render(<OrganizationCreate onCreated={onCreatedMock}/>);

    await waitFor(() => {
      fireEvent.change(getByLabelText(/nazwa/i), { target: { value: 'Gimnazjum nr 2' } });
      fireEvent.change(getByLabelText(/administrator/i), { target: { value: '42' } });
      fireEvent.click(getByRole('button'));

      expect(queryByText(/wszystkie pola są wymagane/i)).toBeNull();
      expect(queryByText(/błąd serwera/i)).toBeNull();
      expect(onCreatedMock).toBeCalledWith(newOrganizationResponse);
    });
  });

  const organizationAdminsResponse = {
    users: [
      {
        id: '42',
        firstName: 'Jan',
        lastName: 'Kowalski',
        role: 'ORGANIZATION_ADMIN',
        username: 'j.kowalski',
        pesel: '10210155874',
        sex: 'MALE',
        email: 'j.kowalski@gmail.com',
        phoneNumber: '+48123456789',
        address: {
          street: 'Szkolna 17',
          postalCode: '15-640',
          city: 'Białystok',
          countryCode: 'PL',
        },
      },
    ],
  };

  const newOrganizationResponse = {
    name: 'Gimnazjum nr 2',
    admin: organizationAdminsResponse.users[0],
    status: 'ENABLED',
  };
});
