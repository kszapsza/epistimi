import './ArticlePage.scss';
import { Alert, Loader } from '@mantine/core';
import { Article } from '../../../dto/article';
import { IconAlertCircle } from '@tabler/icons';
import { useFetch } from '../../../hooks';
import { useParams } from 'react-router-dom';

export const ArticlePage = (): JSX.Element => {
  const { slug } = useParams();
  const { data: article, loading } = useFetch<Article>(`api/article/${slug}`);

  return (
    <>
      {loading && <Loader/>}
      {!article && !loading &&
        <Alert icon={<IconAlertCircle size={16}/>} color="red">
          Nie udało się załadować artykułu!
        </Alert>
      }
      {article &&
        <div className={'article'}>
          <div className={'article-header'}>
            <h2 className={'article-title'}>{article && article.title}</h2>
            <div className={'article-description'}>{article && article.description}</div>
            <div className={'article-metadata'}>19 lutego 2022 | Autor Jakiśtam</div>
          </div>
          <img
            className={'article-image'}
            src={`${process.env.PUBLIC_URL}/images/article-default.jpg`}
            alt={article.title}
          />
          <div className={'article-content'}>
            <p>Sed maximus eu dolor a condimentum. Etiam maximus nibh ut dolor dictum ultrices. Praesent ut porta ex. Curabitur a purus
              aliquam tortor placerat suscipit. Maecenas non dui laoreet, dictum libero ut, sagittis ligula. Mauris malesuada vulputate
              accumsan. Aliquam rutrum dapibus lectus, sit amet condimentum magna rutrum in. Proin porta turpis et ipsum placerat pulvinar.
              Sed magna nulla, mattis id mauris vel, tempus commodo sapien. Suspendisse euismod nec eros vel aliquet. Cras maximus porta
              quam quis euismod. Etiam at ultricies orci, at suscipit nulla. Nunc vitae magna vel mi suscipit semper. Etiam lacinia blandit
              faucibus. Donec vitae sapien lacinia, sollicitudin turpis vel, semper nisl.</p>

            <p>Integer auctor quam cursus sollicitudin pulvinar. Suspendisse potenti. Curabitur finibus, quam at viverra facilisis, odio
              sapien dictum sem, id fermentum ex lorem at augue. In hac habitasse platea dictumst. Etiam sed tristique tellus, et mattis
              lectus. Nulla facilisi. Sed sit amet arcu porta, finibus felis ut, placerat nulla. Etiam non venenatis risus. Etiam sed tortor
              laoreet, elementum urna eget, mattis urna.</p>

            <p>Aliquam nec augue vitae dolor ullamcorper semper quis a sem. Vestibulum tincidunt egestas purus, in laoreet lacus iaculis sit
              amet. Curabitur vitae sollicitudin nulla. Praesent vel luctus quam, eu mattis felis. Pellentesque malesuada, quam et dictum
              fermentum, arcu orci hendrerit orci, vitae lobortis enim dui sed justo. Nunc et molestie neque. Pellentesque in sem eget diam
              euismod vehicula. Pellentesque egestas et erat id venenatis. Proin pellentesque finibus suscipit. Ut nec vulputate ipsum, nec
              tempor felis. Donec ullamcorper id eros id vestibulum. Donec pulvinar lorem turpis, ac varius ligula convallis in. Sed
              dignissim tristique ligula non tristique. Morbi nulla justo, egestas vel sem eget, scelerisque interdum magna.</p>

            <p>Nam eleifend neque sed gravida varius. Praesent laoreet iaculis urna sit amet suscipit. Pellentesque nec nisl lorem. Sed eros
              felis, consectetur sit amet luctus nec, aliquet vitae est. Praesent tristique, massa lacinia eleifend fermentum, eros libero
              pellentesque urna, a imperdiet diam felis id velit. Nulla vel scelerisque ipsum, vel tincidunt nisl. Duis non aliquet turpis,
              at sagittis ipsum. Nullam sit amet nisl quis ipsum luctus commodo vitae nec ex. In eleifend dignissim odio, eu lacinia lorem
              lobortis eu. Aliquam enim nibh, aliquam ac enim sagittis, vulputate hendrerit purus. Donec in aliquam mauris, ut aliquet nisl.
              Duis tincidunt luctus ex lobortis posuere. Vestibulum vitae auctor nibh, non maximus nibh.</p>

            <p>Sed venenatis hendrerit dui at lobortis. Nulla commodo lacus sit amet magna egestas, commodo interdum ex finibus. In hac
              habitasse platea dictumst. Aenean turpis ligula, faucibus in facilisis eget, dapibus vitae augue. In vitae dui eros. Nunc sed
              faucibus nisl. Morbi porta nisl at faucibus porta. Maecenas est risus, vehicula at odio ac, accumsan finibus risus. Fusce at
              erat eu dolor hendrerit semper.</p>
          </div>
        </div>}
    </>
  );
};
