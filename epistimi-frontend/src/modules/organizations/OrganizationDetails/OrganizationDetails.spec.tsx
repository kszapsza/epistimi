import { enabledOrganization } from '../../../stubs/organization';
import { OrganizationDetails } from './OrganizationDetails';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetails component', () => {
  it('should set page title', async () => {
    axiosMock.get.mockResolvedValue({});

    render(<OrganizationDetails/>);

    await waitFor(() => {
      expect(document.title).toBe('Szczegóły placówki – Epistimi');
    });
  });

  it('should properly render component', async () => {
    axiosMock.get.mockResolvedValue({
      data: enabledOrganization,
    });

    const { getAllByRole } = render(<OrganizationDetails/>);

    await waitFor(() => {
      expect(getAllByRole('heading')[0]).toHaveTextContent(/szczegóły placówki/i);
      // TODO: organization details assertions
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
