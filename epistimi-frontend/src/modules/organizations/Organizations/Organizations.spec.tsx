import { Organizations } from './Organizations';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('Organizations component', () => {
  afterEach(() => {
    jest.resetAllMocks();
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
      data: {
        organizations: [
          {
            id: '1',
            name: 'szkoła 1',
            admin: {
              id: '1',
              firstName: 'Bogusław',
              lastName: 'Nowak',
              role: 'ORGANIZATION_ADMIN',
              username: 'b.nowak',
            },
            status: 'DISABLED',
          },
          {
            id: '2',
            name: 'szkoła 2',
            admin: {
              id: '2',
              firstName: 'Zbigniew',
              lastName: 'Bączek',
              role: 'ORGANIZATION_ADMIN',
              username: 'z.baczek',
            },
            status: 'ENABLED',
          },
        ],
      },
    });
    const { queryAllByRole } = render(<Organizations/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/organization');
      expect(queryAllByRole('row')).toHaveLength(3);
      // TODO: to be continued
    });
  });
});
