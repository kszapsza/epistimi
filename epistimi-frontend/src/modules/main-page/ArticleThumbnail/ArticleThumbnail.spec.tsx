import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { ArticleThumbnail } from './ArticleThumbnail';

describe('ArticleThumbnail component', () => {
  it('should render a thumbnail with provided image', async () => {
    const props = {
      image: 'abc.jpg',
      title: 'Article',
      description: 'Description',
    };
    render(<ArticleThumbnail {...props} />);

    await waitFor(() => screen.getByRole('img'));

    expect(screen.getByRole('img')).toHaveAttribute('src', '/images/abc.jpg');
    expect(screen.getByRole('heading')).toHaveTextContent('Article');
    expect(screen.getByRole('description')).toHaveTextContent('Description');
  });

  it('should render a thumbnail with default image if it was not provided', async () => {
    const props = {
      title: 'Article',
      description: 'Description',
    };
    render(<ArticleThumbnail {...props} />);

    await waitFor(() => screen.getByRole('img'));

    expect(screen.getByRole('img')).toHaveAttribute('src', '/images/article-default.jpg');
    expect(screen.getByRole('heading')).toHaveTextContent('Article');
    expect(screen.getByRole('description')).toHaveTextContent('Description');
  });
});
