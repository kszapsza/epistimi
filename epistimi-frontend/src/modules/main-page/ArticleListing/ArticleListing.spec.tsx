import React from 'react';
import axios from 'axios';
import { queryByText, waitFor } from '@testing-library/react';
import { ArticleListing } from './ArticleListing';
import { renderWithRouter } from '../../../util/test-util';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('ArticleListing component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render an error message if API fails to respond', async () => {
    // given
    axiosMock.get.mockRejectedValue({});
    // when
    const { getByText } = renderWithRouter(<ArticleListing/>);
    // then
    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('article');
      expect(getByText(/nie udało się załadować artykułów/i)).toBeInTheDocument();
    });
  });

  it('should render an error message if there are no articles', async () => {
    // given
    axiosMock.get.mockResolvedValue({
      data: {
        articles: []
      }
    });
    // when
    const { getByText } = renderWithRouter(<ArticleListing/>);
    // then
    await waitFor(() => {
      expect(getByText(/nie udało się załadować artykułów/i)).toBeInTheDocument();
    });
  });

  it('should render a listing with provided articles', async () => {
    // given
    axiosMock.get.mockResolvedValue({
      data: {
        articles: [
          {
            id: '123',
            title: 'xyz',
            description: 'abc'
          },
          {
            id: '456',
            title: 'foo',
            description: 'bar'
          }
        ]
      }
    });
    // when
    const { getAllByRole, queryByText } = renderWithRouter(<ArticleListing/>);
    // then
    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('article');
      expect(queryByText(/nie udało się załadować artykułów/i)).toBeNull();
      expect(getAllByRole('heading')[0]).toHaveTextContent('xyz');
      expect(getAllByRole('heading')[1]).toHaveTextContent('foo');
    });
  });
});
