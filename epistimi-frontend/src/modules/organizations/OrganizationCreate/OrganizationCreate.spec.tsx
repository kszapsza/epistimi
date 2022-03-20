import { fireEvent, waitFor } from '@testing-library/react';
import { Matcher } from '@testing-library/dom/types/matches';
import { OrganizationCreate } from './OrganizationCreate';
import { OrganizationRegisterRequest, OrganizationResponse, OrganizationStatus } from '../../../dto/organization';
import { render } from '../../../utils/test-render';
import { UserRole, UserSex } from '../../../dto/user';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationCreate component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render form properly', async () => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    const { getByLabelText, getByRole, queryByText } = render(<OrganizationCreate onCreated={jest.fn()}/>);

    await waitFor(() => {
      const input = getByLabelText(/nazwa/i) as HTMLInputElement;
      const administratorSelect = getByLabelText(/administrator/i) as HTMLSelectElement;
      const directorSelect = getByLabelText(/dyrektor/i) as HTMLSelectElement;
      const button = getByRole('button') as HTMLButtonElement;

      expect(axiosMock.get).toHaveBeenCalledTimes(2);
      expect(input).toBeInTheDocument();

      expect(administratorSelect).toBeInTheDocument();
      expect(administratorSelect.options).toHaveLength(2);
      expect(administratorSelect.options[0].value).toBe('');
      expect(administratorSelect.options[0].innerHTML).toBe('');
      expect(administratorSelect.options[1].value).toBe('42');
      expect(administratorSelect.options[1].innerHTML).toBe('Kowalski Jan (j.kowalski)');

      expect(directorSelect).toBeInTheDocument();
      expect(directorSelect.options).toHaveLength(2);
      expect(directorSelect.options[0].value).toBe('');
      expect(directorSelect.options[0].innerHTML).toBe('');
      expect(directorSelect.options[1].value).toBe('43');
      expect(directorSelect.options[1].innerHTML).toBe('Kowalska Adrianna (a.kowalska)');

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
      fireEvent.click(getByRole('button'));

      expect(getByText(/wszystkie pola są wymagane/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it('should show an error message if server fails to respond', async () => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });
    axiosMock.post.mockRejectedValue({});

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, getByText } = render(<OrganizationCreate onCreated={onCreatedMock}/>);

    await waitFor(() => {
      fillOutWholeForm(getByLabelText);
      fireEvent.click(getByRole('button'));

      expect(getByText(/błąd serwera/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it('should call `onCreated` callback on successful organization creation', async () => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    const postResponse: OrganizationResponse = {
      id: '123',
      name: 'SP7',
      admin: organizationAdminsResponse.users[0],
      director: organizationDirectorsResponse.users[0],
      status: OrganizationStatus.ENABLED,
      address: {
        street: 'Wąska 101',
        postalCode: '41-215',
        city: 'Sosnowiec',
        countryCode: 'PL',
      },
    };
    axiosMock.post.mockResolvedValue({ data: postResponse });

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, queryByText } = render(<OrganizationCreate onCreated={onCreatedMock}/>);

    await waitFor(() => {
      fillOutWholeForm(getByLabelText);
    });

    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(/wszystkie pola są wymagane/i)).toBeNull();
      expect(queryByText(/błąd serwera/i)).toBeNull();

      expect(axiosMock.post).toHaveBeenCalledWith('/api/organization', {
        name: 'SP7',
        adminId: '42',
        directorId: '43',
        address: {
          street: 'Wąska 101',
          postalCode: '41-215',
          city: 'Sosnowiec',
          countryCode: 'PL',
        },
      } as OrganizationRegisterRequest);

      expect(onCreatedMock).toBeCalledWith(postResponse);
    });
  });

  const fillOutWholeForm = (
    getByLabelText: (arg: Matcher) => HTMLElement
  ): void => {
    fireEvent.change(getByLabelText(/nazwa/i), { target: { value: 'SP7' } });
    fireEvent.change(getByLabelText(/administrator/i), { target: { value: '42' } });
    fireEvent.change(getByLabelText(/dyrektor/i), { target: { value: '43' } });
    fireEvent.change(getByLabelText(/ulica/i), { target: { value: 'Wąska 101' } });
    fireEvent.change(getByLabelText(/kod pocztowy/i), { target: { value: '41-215' } });
    fireEvent.change(getByLabelText(/miasto/i), { target: { value: 'Sosnowiec' } });
    fireEvent.change(getByLabelText(/kraj/i), { target: { value: 'PL' } });
  };

  const organizationAdminsResponse = {
    users: [
      {
        id: '42',
        firstName: 'Jan',
        lastName: 'Kowalski',
        role: UserRole.ORGANIZATION_ADMIN,
        username: 'j.kowalski',
        pesel: '10210155874',
        sex: UserSex.MALE,
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

  const organizationDirectorsResponse = {
    users: [
      {
        id: '43',
        firstName: 'Adrianna',
        lastName: 'Kowalska',
        role: UserRole.TEACHER,
        username: 'a.kowalska',
        pesel: '10210155874',
        sex: UserSex.FEMALE,
        email: 'a.kowalska@outlook.com',
        phoneNumber: '+48987654321',
        address: {
          street: 'Świętego Andrzeja Boboli 10',
          postalCode: '15-649',
          city: 'Białystok',
          countryCode: 'PL',
        },
      },
    ],
  };
});
