import { enabledOrganization } from '../../../stubs/organization';
import { fireEvent, waitFor } from '@testing-library/react';
import { Matcher } from '@testing-library/dom/types/matches';
import { OrganizationEdit, OrganizationEditVariant } from './OrganizationEdit';
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

  it.each([
    [OrganizationEditVariant.CREATE, 'Utwórz'],
    [OrganizationEditVariant.UPDATE, 'Zapisz'],
  ])('should render form properly (%s)', async (
    variant: OrganizationEditVariant,
    buttonLabel: string,
  ) => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    const { getByLabelText, getByRole, queryByText } = render(
      <OrganizationEdit submitCallback={jest.fn()} variant={variant}/>,
    );

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
      expect(button.innerHTML).toBe(buttonLabel);

      expect(queryByText(/wszystkie pola są wymagane/i)).toBeNull();
      expect(queryByText(/błąd serwera/i)).toBeNull();
    });
  });

  it.each([
    OrganizationEditVariant.CREATE,
    OrganizationEditVariant.UPDATE,
  ])('should show an error message if form is empty (%s)', async (
    variant: OrganizationEditVariant,
  ) => {
    axiosMock.get.mockResolvedValue({
      data: organizationAdminsResponse,
    });

    const onCreatedMock = jest.fn();
    const { getByRole, getByText } = render(
      <OrganizationEdit submitCallback={onCreatedMock} variant={variant}/>,
    );

    await waitFor(() => {
      fireEvent.click(getByRole('button'));

      expect(getByText(/wszystkie pola są wymagane/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it.each([
    [OrganizationEditVariant.CREATE, axiosMock.post],
    [OrganizationEditVariant.UPDATE, axiosMock.put],
  ])('should show an error message if server fails to respond (%s)', async (
    variant: OrganizationEditVariant,
    axiosActionMock,
  ) => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    axiosActionMock.mockRejectedValue({});

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, getByText } = render(
      <OrganizationEdit submitCallback={onCreatedMock} variant={variant}/>,
    );

    await waitFor(() => {
      fillOutWholeForm(getByLabelText);
      fireEvent.click(getByRole('button'));

      expect(getByText(/błąd serwera/i)).toBeInTheDocument();
      expect(onCreatedMock).not.toBeCalled();
    });
  });

  it.each([
    [OrganizationEditVariant.CREATE, axiosMock.post, '/api/organization'],
    [OrganizationEditVariant.UPDATE, axiosMock.put, '/api/organization/123'],
  ])('should submit valid form and call `submitCallback` callback with server response (%s)', async (
    variant: OrganizationEditVariant,
    axiosActionMock,
    endpoint: string,
  ) => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    const submitResponse: OrganizationResponse = {
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
    axiosActionMock.mockResolvedValue({ data: submitResponse });

    const onCreatedMock = jest.fn();
    const { getByLabelText, getByRole, queryByText } = render(
      <OrganizationEdit
        submitCallback={onCreatedMock}
        variant={variant}
        organizationId={'123'}
      />);

    await waitFor(() => {
      fillOutWholeForm(getByLabelText);
    });

    fireEvent.click(getByRole('button'));

    await waitFor(() => {
      expect(queryByText(/wszystkie pola są wymagane/i)).toBeNull();
      expect(queryByText(/błąd serwera/i)).toBeNull();

      expect(axiosActionMock).toHaveBeenCalledWith(endpoint, {
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

      expect(onCreatedMock).toBeCalledWith(submitResponse);
    });
  });

  it('should fill out the form with default values if provided', async () => {
    axiosMock.get.mockResolvedValueOnce({
      data: organizationAdminsResponse,
    });
    axiosMock.get.mockResolvedValueOnce({
      data: organizationDirectorsResponse,
    });

    const { getByLabelText } = render(
      <OrganizationEdit
        submitCallback={jest.fn()}
        variant={OrganizationEditVariant.UPDATE}
        organizationId={'123'}
        defaults={enabledOrganization}
      />);

    await waitFor(() => {
      expect(getByLabelText(/nazwa/i)).toHaveValue('SP7');
      expect(getByLabelText(/administrator/i)).toHaveValue('42');
      expect(getByLabelText(/dyrektor/i)).toHaveValue('43');
      expect(getByLabelText(/ulica/i)).toHaveValue('Wrocławska 5');
      expect(getByLabelText(/kod pocztowy/i)).toHaveValue('15-644');
      expect(getByLabelText(/miasto/i)).toHaveValue('Białystok');
      expect(getByLabelText(/kraj/i)).toHaveValue('PL');
    });
  });

  const fillOutWholeForm = (
    getByLabelText: (arg: Matcher) => HTMLElement,
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
})
;
