import { disabledOrganization, enabledOrganization } from '../../../stubs/organization';
import { OrganizationDetails } from './OrganizationDetails';
import { OrganizationResponse } from '../../../dto/organization';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetails component', () => {
  it.each([
    ['Dezaktywuj placówkę', enabledOrganization],
    ['Aktywuj placówkę', disabledOrganization],
  ])('should render status change button (%s)', async (
    statusButtonLabel: string,
    mockData: OrganizationResponse,
  ) => {
    axiosMock.get.mockResolvedValue({
      data: mockData,
    });

    const { queryAllByRole } = render(<OrganizationDetails/>);

    await waitFor(() => {
      const buttons = queryAllByRole('button') as HTMLButtonElement[];
      expect(buttons[0]).toHaveTextContent(statusButtonLabel);
      expect(buttons[1]).toHaveTextContent('Edytuj dane');
    });
  });

  it('should render an error message if server fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(<OrganizationDetails/>);

    await waitFor(() => {
      expect(getByText(/nie udało się załadować szczegółów organizacji/i)).toBeInTheDocument();
    });
  });
});
