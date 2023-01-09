import { enabledOrganization } from '../../../stubs/organization';
import { OrganizationDetails } from './OrganizationDetails';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetails component', () => {

  const EDIT_DATA_REGEXP = /organizations\.organizationDetails\.editData/;
  const COULD_NOT_LOAD_REGEXP = /organizations\.organizationDetails\.couldNotLoadDetails/;

  it('should render status change button (%s)', async () => {
    axiosMock.get.mockResolvedValue({ data: enabledOrganization });

    const { queryAllByRole } = render(<OrganizationDetails/>);

    await waitFor(() => {
      const buttons = queryAllByRole('button') as HTMLButtonElement[];
      expect(buttons[0]).toHaveTextContent(EDIT_DATA_REGEXP);
    });
  });

  it('should render an error message if server fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(<OrganizationDetails/>);

    await waitFor(() => {
      expect(getByText(COULD_NOT_LOAD_REGEXP)).toBeInTheDocument();
    });
  });
});
