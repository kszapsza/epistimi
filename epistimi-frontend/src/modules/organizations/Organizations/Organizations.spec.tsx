import { disabledOrganization, enabledOrganization } from '../../../stubs/organization';
import { fireEvent, waitFor } from '@testing-library/react';
import { Organizations } from './Organizations';
import { OrganizationsResponse } from '../../../dto/organization';
import { render } from '../../../utils/test-render';
import { UserRole, UserSex, UsersResponse } from '../../../dto/user';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('Organizations component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should set page title', async () => {
    axiosMock.get.mockResolvedValue({});

    render(<Organizations/>);

    await waitFor(() => {
      expect(document.title).toBe('Placówki – Epistimi');
    });
  });

  it('should render an error message if organizations could not be fetched from API', async () => {
    axiosMock.get.mockRejectedValue({});

    const { queryByText } = render(<Organizations/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/organization');
      expect(queryByText(/nie udało się załadować listy placówek/i)).toBeInTheDocument();
    });
  });

  it('should render a table with organizations', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationsResponse,
    });
    const { queryAllByRole } = render(<Organizations/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/organization');
      expect(queryAllByRole('row')).toHaveLength(3);
      // TODO: to be continued
    });
  });

  it('should open create organization modal window on button click', async () => {
    axiosMock.get
      .mockResolvedValueOnce({ data: organizationsResponse })
      .mockResolvedValueOnce({ data: organizationAdminsResponse });

    const { getByText } = render(<Organizations/>);

    await waitFor(() => {
      const modalButton = getByText(/utwórz nową/i) as HTMLButtonElement;
      fireEvent.click(modalButton);

      const modalWindowHeader = getByText(/tworzenie nowej placówki/i);
      expect(modalWindowHeader).toBeInTheDocument();
    });
  });

  const organizationsResponse: OrganizationsResponse = {
    organizations: [
      disabledOrganization,
      enabledOrganization,
    ],
  };

  const organizationAdminsResponse: UsersResponse = {
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
});
