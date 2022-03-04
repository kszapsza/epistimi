import { ArticleThumbnail } from '../ArticleThumbnail';
import { Articles } from '../../../dto/article';
import { MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { useFetch } from '../../../hooks/useFetch';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

export const ArticleListing = (): JSX.Element => {
  const { data, loading } = useFetch<Articles>('api/article');

  return (
    <>
      {loading && <Spinner/>}
      {(!data || data.articles.length === 0) && !loading &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutlineIcon/>}>
          Nie udało się załadować artykułów!
        </MessageBox>}
      {data && data.articles.map((article, idx) =>
        <ArticleThumbnail key={`article-${idx}`} {...article} />
      )}
    </>
  );
};
