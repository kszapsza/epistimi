import { ArticleThumbnail } from './ArticleThumbnail';
import { render } from '../../../utils/test-render';

describe('ArticleThumbnail component', () => {
  it('should render a thumbnail with provided image', () => {
    const props = {
      id: '123',
      slug: 'article',
      image: 'abc.jpg',
      title: 'Article',
      description: 'Description',
    };

    const { getByRole } = render(<ArticleThumbnail {...props} />);

    expect(getByRole('img')).toHaveAttribute('src', '/images/abc.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });

  it('should render a thumbnail with default image if it was not provided', () => {
    const props = {
      id: '234',
      slug: 'article',
      title: 'Article',
      description: 'Description',
    };

    const { getByRole } = render(<ArticleThumbnail {...props} />);

    expect(getByRole('img')).toHaveAttribute('src', '/images/article-default.jpg');
    expect(getByRole('heading')).toHaveTextContent('Article');
    expect(getByRole('definition')).toHaveTextContent('Description');
  });
});
