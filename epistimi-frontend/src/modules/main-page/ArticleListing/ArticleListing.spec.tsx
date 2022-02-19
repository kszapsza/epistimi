import React from 'react';
import axios from 'axios';
import { queryByText, render, waitFor } from '@testing-library/react';
import { ArticleListing } from './ArticleListing';
import { MemoryRouter } from 'react-router-dom';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('ArticleListing component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render an error message if API fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(
      <MemoryRouter>
        <ArticleListing/>
      </MemoryRouter>
    );


    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('article');
      expect(getByText(/nie udało się załadować artykułów/i)).toBeInTheDocument();
    });
  });

  it('should render an error message if there are no articles', async () => {
    axiosMock.get.mockResolvedValue({
      data: {
        articles: []
      }
    });

    const { getByText } = render(
      <MemoryRouter>
        <ArticleListing/>
      </MemoryRouter>
    );

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

    const { getAllByRole, queryByText } = render(
      <MemoryRouter>
        <ArticleListing/>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('article');
      expect(queryByText(/nie udało się załadować artykułów/i)).toBeNull();
      expect(getAllByRole('heading')[0]).toHaveTextContent('xyz');
      expect(getAllByRole('definition')[0]).toHaveTextContent('abc');
      expect(getAllByRole('heading')[1]).toHaveTextContent('foo');
      expect(getAllByRole('definition')[1]).toHaveTextContent('bar');
    });
  });
});
