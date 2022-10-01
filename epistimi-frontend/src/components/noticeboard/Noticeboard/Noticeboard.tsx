import './Noticeboard.scss';
import { Alert, Modal } from '@mantine/core';
import { IconAlertCircle, IconCheck } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { NoticeboardHeader } from '../NoticeboardHeader';
import { NoticeboardPost, NoticeboardPostForm, NoticeboardPostFormVariant } from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';
import axios from 'axios';

export const Noticeboard = (): JSX.Element => {
  const { t } = useTranslation();
  const { data, error, loading, reload } = useFetch<NoticeboardPostResponse>('/api/noticeboard/post');

  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);
  const [deletedMessageOpened, deletedMessageHandlers] = useDisclosure(false);

  useDocumentTitle(t('noticeboard.noticeboard.title'));

  const onNoticeboardPostCreated = (): void => {
    reload();
    createModalHandlers.close();
    createdMessageHandlers.open();
  };

  const onLikeClick = (postId: string) => {
    // TODO
    console.log(`like: ${postId}`);
  };

  const onEditClick = (postId: string) => {
    // TODO
    console.log(`edit: ${postId}`);
  };

  const onDeleteClick = (postId: string) => {
    // TODO: confirmation modal
    axios.delete(`/api/noticeboard/post/${postId}`)
      .then(() => {
        reload();
        deletedMessageHandlers.open();
      });
  };

  return (
    <>
      <Modal
        onClose={createModalHandlers.close}
        opened={createModalOpened}
        size={'lg'}
        title={t('noticeboard.noticeboard.createNewPostModalTitle')}
      >
        <NoticeboardPostForm
          onSubmit={onNoticeboardPostCreated}
          variant={NoticeboardPostFormVariant.CREATE}
        />
      </Modal>

      <div className={'noticeboard'}>
        <NoticeboardHeader onCreatePostClick={createModalHandlers.open} />

        {loading && <LoaderBox/>}

        {error && (
          <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
            {t('noticeboard.noticeboard.couldNotLoadPosts')}
          </Alert>
        )}
        {createdMessageOpened && (
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t('noticeboard.noticeboard.newPostCreated')}
          </Alert>
        )}
        {deletedMessageOpened && (
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t('noticeboard.noticeboard.postDeleted')}
          </Alert>
        )}

        {data && (
          <div className={'noticeboard-posts'}>
            {data.posts.map((post) =>
              <NoticeboardPost
                key={post.id}
                post={post}
                onLikeClick={() => onLikeClick(post.id)}
                onEditClick={() => onEditClick(post.id)}
                onDeleteClick={() => onDeleteClick(post.id)}
              />)}
          </div>
        )}
      </div>
    </>
  );
};