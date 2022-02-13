import React from 'react';
import { ArticleThumbnail } from './ArticleThumbnail';
import { renderWithRouter } from '../../../util/test-util';

describe('ArticleThumbnail component', () => {
  it('should render a thumbnail with provided image', async () => {
    // given
    const props = {
      id: '123',
      slug: 'article',
      image: 'abc.jpg',
      title: 'Article',
      description: 'Description',
    };
    // when
    const { getByRole } = renderWithRouter(<ArticleThumbnail {...props} />);
    // then
    expect(getByRole('img')).toHaveAttribute('src', '/images/abc.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });

  it('should render a thumbnail with default image if it was not provided', async () => {
    // given
    const props = {
      id: '234',
      slug: 'article',
      title: 'Article',
      description: 'Description',
    };
    // when
    const { getByRole } = renderWithRouter(<ArticleThumbnail {...props} />);
    // then
    expect(getByRole('img')).toHaveAttribute('src', '/images/article-default.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });
});
