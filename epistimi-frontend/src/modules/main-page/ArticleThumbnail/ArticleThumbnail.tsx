import './ArticleThumbnail.scss';

interface ArticleThumbnailProps {
  image?: string;
  title: string;
  description: string;
}

const ArticleThumbnail = (props: ArticleThumbnailProps): JSX.Element => {
  return (
    <div className={'article-thumbnail'}>
      {props.image && <div className={'article-thumbnail-image'}>
        <img alt={props.title} src={`${process.env.PUBLIC_URL}/images/${props.image}`}/>
      </div>}
      <div className={'article-thumbnail-metadata'}>
        <div className={'article-thumbnail-title'}>
          {props.title}
        </div>
        <div className={'article-thumbnail-description'}>
          {props.description}
        </div>
      </div>
    </div>
  )
};

export default ArticleThumbnail;
