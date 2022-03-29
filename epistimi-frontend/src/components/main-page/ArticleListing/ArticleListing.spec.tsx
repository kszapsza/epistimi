import { ArticleListing } from './ArticleListing';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('ArticleListing component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render an error message if API fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(<ArticleListing/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/article');
      expect(getByText(/nie udało się załadować artykułów/i)).toBeInTheDocument();
    });
  });

  it('should render an error message if there are no articles', async () => {
    axiosMock.get.mockResolvedValue({
      data: {
        articles: [],
      },
    });

    const { getByText } = render(<ArticleListing/>);

    await waitFor(() => {
      expect(getByText(/nie udało się załadować artykułów/i)).toBeInTheDocument();
    });
  });

  it('should render a listing with provided articles', async () => {
    axiosMock.get.mockResolvedValue({
      data: {
        articles: [
          {
            id: '123',
            title: 'xyz',
            description: 'abc',
          },
          {
            id: '456',
            title: 'foo',
            description: 'bar',
          },
        ],
      },
    });

    const { getAllByRole, queryByText } = render(<ArticleListing/>);

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/article');
      expect(queryByText(/nie udało się załadować artykułów/i)).toBeNull();
      expect(getAllByRole('heading')[0]).toHaveTextContent('xyz');
      expect(getAllByRole('definition')[0]).toHaveTextContent('abc');
      expect(getAllByRole('heading')[1]).toHaveTextContent('foo');
      expect(getAllByRole('definition')[1]).toHaveTextContent('bar');
    });
  });
});
