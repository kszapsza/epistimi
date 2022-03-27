import { disabledOrganization, enabledOrganization } from '../../../stubs/organization';
import { fireEvent, waitFor } from '@testing-library/react';
import { OrganizationsListing } from './OrganizationsListing';
import { OrganizationsResponse } from '../../../dto/organization';
import { render } from '../../../utils/test-render';
import { UserRole, UserSex, UsersResponse } from '../../../dto/user';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationsListing component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should set page title', async () => {
    axiosMock.get.mockResolvedValue({});

    render(<OrganizationsListing/>);

    await waitFor(() => {
      expect(document.title).toBe('Placówki – Epistimi');
    });
  });

  it('should render an error message if organizations could not be fetched from API', async () => {
    axiosMock.get.mockRejectedValue({});

    const { queryByText } = render(<OrganizationsListing/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/organization');
      expect(queryByText(/nie udało się załadować listy placówek/i)).toBeInTheDocument();
    });
  });

  it('should render a table with organizations', async () => {
    axiosMock.get.mockResolvedValue({
      data: organizationsResponse,
    });
    const { queryAllByRole } = render(<OrganizationsListing/>);

    await waitFor(() => {
      const rows = queryAllByRole('row');

      expect(axiosMock.get).toHaveBeenCalledWith('api/organization');
      expect(rows).toHaveLength(2);
      expect(rows[0]).toHaveTextContent(/sp7/i);
      expect(rows[0]).toHaveTextContent(/nieaktywna/i);
      expect(rows[1]).toHaveTextContent(/sp7/i);
      expect(rows[1]).toHaveTextContent(/aktywna/i);
    });
  });

  it('should open create organization modal window on button click', async () => {
    axiosMock.get
      .mockResolvedValueOnce({ data: organizationsResponse })
      .mockResolvedValue({ data: organizationAdminsResponse });

    const { getByText } = render(<OrganizationsListing/>);

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
