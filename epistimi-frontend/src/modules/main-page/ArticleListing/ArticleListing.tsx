import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import { useEffect, useState } from 'react';
import { ArticleThumbnail } from '../ArticleThumbnail';
import { MessageBox, MessageBoxStyle, Spinner } from '../../../shared';
import { Article, Articles } from '../../../dto/article';
import axios from 'axios';

export const ArticleListing = (): JSX.Element => {
  const [articles, setArticles] = useState<Article[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect((): void => {
    axios.get<Articles>('article')
      .then((response) => {
          setArticles(response.data.articles);
          setLoading(false);
      })
      .catch(() => {
        setArticles([]);
        setLoading(false);
      });
  }, [setArticles, setLoading]);

  return (
    <>
      {loading && <Spinner/>}
      {(!articles || articles.length === 0) && !loading &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutlineIcon/>}>
          Nie udało się załadować artykułów!
        </MessageBox>}
      {articles.map((article, idx) =>
        <ArticleThumbnail key={`article-${idx}`} {...article} />
      )}
    </>
  )
};
