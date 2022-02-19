import { Article } from '../../../dto/article';
import { useParams } from 'react-router-dom';
import { MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { useFetch } from '../../../hooks/useFetch';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

export const ArticlePage = (): JSX.Element => {
  const { slug } = useParams();
  const { data: article, loading } = useFetch<Article>(`article/${slug}`);

  return (
    <>
      {loading && <Spinner/>}
      {!article && !loading &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutlineIcon/>}>
          Nie udało się załadować artykułu!
        </MessageBox>}
      {article &&
        <div className="article">
          <h2>{article && article.title}</h2>
          <p>{article && article.description}</p>
        </div>}
    </>
  );
};
