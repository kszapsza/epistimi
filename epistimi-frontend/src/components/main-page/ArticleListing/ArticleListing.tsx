import { Alert, Loader } from '@mantine/core';
import { Articles } from '../../../dto/article';
import { ArticleThumbnail } from '../ArticleThumbnail';
import { ErrorOutline } from '@mui/icons-material';
import { useFetch } from '../../../hooks/useFetch';

export const ArticleListing = (): JSX.Element => {
  const { data, loading } = useFetch<Articles>('api/article');

  return (
    <>
      {loading && <Loader/>}
      {(!data || data.articles.length === 0) && !loading &&
        <Alert icon={<ErrorOutline style={{ fontSize: '16px' }}/>} color="red">
          Nie udało się załadować artykułów!
        </Alert>}
      {data && data.articles.map((article, idx) =>
        <ArticleThumbnail key={`article-${idx}`} {...article} />
      )}
    </>
  );
};
