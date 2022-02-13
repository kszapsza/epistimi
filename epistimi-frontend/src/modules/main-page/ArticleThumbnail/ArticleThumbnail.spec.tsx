import React from 'react';
import { render } from '@testing-library/react';
import { ArticleThumbnail } from './ArticleThumbnail';

describe('ArticleThumbnail component', () => {
  it('should render a thumbnail with provided image', async () => {
    // given
    const props = {
      image: 'abc.jpg',
      title: 'Article',
      description: 'Description',
    };

    // when
    const { getByRole } = render(<ArticleThumbnail {...props} />);

    // then
    expect(getByRole('img')).toHaveAttribute('src', '/images/abc.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('description')).toHaveTextContent('Description');
  });

  it('should render a thumbnail with default image if it was not provided', async () => {
    // given
    const props = {
      title: 'Article',
      description: 'Description',
    };

    // when
    const { getByRole } = render(<ArticleThumbnail {...props} />);

    // then
    expect(getByRole('img')).toHaveAttribute('src', '/images/article-default.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('description')).toHaveTextContent('Description');
  });
});
