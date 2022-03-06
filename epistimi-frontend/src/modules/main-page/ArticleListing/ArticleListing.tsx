import { Articles } from '../../../dto/article';
import { ArticleThumbnail } from '../ArticleThumbnail';
import { ErrorOutline } from '@mui/icons-material';
import { MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { useFetch } from '../../../hooks/useFetch';

export const ArticleListing = (): JSX.Element => {
  const { data, loading } = useFetch<Articles>('api/article');

  return (
    <>
      {loading && <Spinner/>}
      {(!data || data.articles.length === 0) && !loading &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutline/>}>
          Nie udało się załadować artykułów!
        </MessageBox>}
      {data && data.articles.map((article, idx) =>
        <ArticleThumbnail key={`article-${idx}`} {...article} />
      )}
    </>
  );
};
