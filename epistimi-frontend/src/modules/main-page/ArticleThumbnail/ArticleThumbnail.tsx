import { Link } from 'react-router-dom';
import './ArticleThumbnail.scss';

interface ArticleThumbnailProps {
  id: string;
  slug: string;
  image?: string;
  title: string;
  description: string;
}

export const ArticleThumbnail = (props: ArticleThumbnailProps): JSX.Element => {
  const image = props.image ?? 'article-default.jpg';
  return (
    <div className={'article-thumbnail'}>
      <Link to={`/article/${props.slug}`}>
        <div className={'article-thumbnail-image'}>
          <img alt={props.title} src={`${process.env.PUBLIC_URL}/images/${image}`}/>
        </div>
        <div className={'article-thumbnail-metadata'}>
          <h3 className={'article-thumbnail-title'}>
            {props.title}
          </h3>
          <div className={'article-thumbnail-description'} role={'definition'}>
            {props.description}
          </div>
        </div>
      </Link>
    </div>
  )
};
