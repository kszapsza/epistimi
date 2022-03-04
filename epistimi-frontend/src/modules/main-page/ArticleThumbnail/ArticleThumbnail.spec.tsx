import { ArticleThumbnail } from './ArticleThumbnail';
import { MemoryRouter } from 'react-router-dom';
import { render } from '@testing-library/react';
import React from 'react';

describe('ArticleThumbnail component', () => {
  it('should render a thumbnail with provided image', async () => {
    const props = {
      id: '123',
      slug: 'article',
      image: 'abc.jpg',
      title: 'Article',
      description: 'Description',
    };

    const { getByRole } = render(
      <MemoryRouter>
        <ArticleThumbnail {...props} />
      </MemoryRouter>
    );

    expect(getByRole('img')).toHaveAttribute('src', '/images/abc.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });

  it('should render a thumbnail with default image if it was not provided', async () => {
    const props = {
      id: '234',
      slug: 'article',
      title: 'Article',
      description: 'Description',
    };

    const { getByRole } = render(
      <MemoryRouter>
        <ArticleThumbnail {...props} />
      </MemoryRouter>
    );

    // TODO: test links!
    expect(getByRole('img')).toHaveAttribute('src', '/images/article-default.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });
});
