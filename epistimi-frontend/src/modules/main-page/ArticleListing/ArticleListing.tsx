import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';
import { ArticleThumbnail } from '../ArticleThumbnail';
import { MessageBox, MessageBoxStyle } from '../../shared';
import { useEffect, useState } from 'react';
import { Article, Articles } from '../../../dto/article';
import axios from 'axios';

export const ArticleListing = (): JSX.Element => {
  const [articles, setArticles] = useState<Article[]>([]);

  useEffect((): void => {
    axios.get<Articles>('article')
      .then((response) => setArticles(response.data.articles))
      .catch(() => setArticles([]));
  }, [setArticles]);

  return (
    <>
      {(!articles || articles.length === 0) &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutlineIcon/>}>
          Nie udało się załadować artykułów!
        </MessageBox>}
      {articles.map((article, idx) =>
        <ArticleThumbnail key={`article-${idx}`} {...article} />
      )}
    </>
  )
};
