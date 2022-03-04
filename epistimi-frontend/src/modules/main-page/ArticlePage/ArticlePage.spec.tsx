import React from 'react';
import axios from 'axios';
import { ArticlePage } from './ArticlePage';
import { render, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('ArticlePage component', () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it('should render an error if API fails to respond', async () => {
    axiosMock.get.mockRejectedValue({});

    const { getByText } = render(
      <MemoryRouter initialEntries={['/article/test-slug']}>
        <Routes>
          <Route path="/article/:slug" element={<ArticlePage/>}/>
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/article/test-slug');
      expect(getByText(/nie udało się załadować artykułu/i)).toBeInTheDocument();
    });
  });

  it('should properly render an article', async () => {
    axiosMock.get.mockResolvedValue({
      data: {
        id: '42',
        slug: 'some-slug',
        title: 'Lorem ipsum',
        description: 'dolor sit amet'
      }
    });

    const { getByRole, getByText } = render(
      <MemoryRouter initialEntries={['/article/some-slug']}>
        <Routes>
          <Route path="/article/:slug" element={<ArticlePage/>}/>
        </Routes>
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(axiosMock.get).toHaveBeenCalledWith('api/article/some-slug');
      expect(getByRole('heading')).toHaveTextContent(/lorem ipsum/i);
      expect(getByText(/dolor sit amet/i)).toBeInTheDocument();
    });
  });
});
