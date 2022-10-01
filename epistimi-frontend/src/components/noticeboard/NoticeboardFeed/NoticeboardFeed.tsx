import './NoticeboardFeed.scss';
import { NoticeboardFeedPost } from '../NoticeboardFeedPost';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { Title } from '@mantine/core';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const NoticeboardFeed = (): JSX.Element => {
  const { t } = useTranslation();
  const { data, error, loading } = useFetch<NoticeboardPostResponse>('/api/noticeboard/post');

  useDocumentTitle(t('noticeboard.noticeboardFeed.title'));

  // TODO: handle backend failures
  // TODO: form for creating a new noticeboard post

  return (
    <div className={'noticeboard'}>
      <div className={'noticeboard-header'}>
        <Title order={2}>{t('noticeboard.noticeboardFeed.title')}</Title>
      </div>

      <div className={'noticeboard-posts'}>
        {data?.posts.map((post) =>
          <NoticeboardFeedPost key={post.id} post={post}/>)}
      </div>
    </div>
  );
};