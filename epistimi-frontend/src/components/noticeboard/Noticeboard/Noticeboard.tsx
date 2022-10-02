import './Noticeboard.scss';
import { Alert, Modal } from '@mantine/core';
import { IconAlertCircle, IconCheck, IconInfoCircle } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { NoticeboardHeader } from '../NoticeboardHeader';
import {
  NoticeboardPost,
  NoticeboardPostDeleteConfirmation,
  NoticeboardPostForm,
  NoticeboardPostFormVariant,
} from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

export const Noticeboard = (): JSX.Element => {
  const { t } = useTranslation();
  const { data, error, loading, reload } = useFetch<NoticeboardPostResponse>('/api/noticeboard/post');

  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);
  const [deleteModalOpened, deleteModalHandlers] = useDisclosure(false);
  const [deletedMessageOpened, deletedMessageHandlers] = useDisclosure(false);
  const [editModalOpened, editModalHandlers] = useDisclosure(false);
  const [updatedMessageOpened, updatedMessageHandlers] = useDisclosure(false);

  const [deletedPost, setDeletedPost] = useState<NoticeboardPostResponse | null>(null);
  const [editedPost, setEditedPost] = useState<NoticeboardPostResponse | null>(null);

  useDocumentTitle(t('noticeboard.noticeboard.title'));

  const onNoticeboardPostCreated = (): void => {
    reload();
    createModalHandlers.close();
    createdMessageHandlers.open();
  };

  const onEditClick = (post: NoticeboardPostResponse) => {
    setEditedPost(post);
    editModalHandlers.open();
  };

  const onNoticeboardPostUpdated = (): void => {
    reload();
    editModalHandlers.close();
    updatedMessageHandlers.open();
  };

  const onDeleteClick = (post: NoticeboardPostResponse): void => {
    setDeletedPost(post);
    deleteModalHandlers.open();
  };

  const afterDelete = (): void => {
    deleteModalHandlers.close();
    deletedMessageHandlers.open();
    reload();
  };

  const onDeleteCancel = (): void => {
    deleteModalHandlers.close();
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

      {deletedPost && <Modal
        onClose={deleteModalHandlers.close}
        opened={deleteModalOpened}
        size={'lg'}
        title={t('noticeboard.noticeboard.deletePostModalTitle')}
      >
        <NoticeboardPostDeleteConfirmation
          post={deletedPost}
          afterDelete={afterDelete}
          onCancel={onDeleteCancel}
        />
      </Modal>}

      {editedPost && <Modal
        onClose={editModalHandlers.close}
        opened={editModalOpened}
        size={'lg'}
        title={t('noticeboard.noticeboard.editPostModalTitle')}
      >
        <NoticeboardPostForm
          onSubmit={onNoticeboardPostUpdated}
          variant={NoticeboardPostFormVariant.UPDATE}
          post={editedPost}
        />
      </Modal>}

      <div className={'noticeboard'}>
        <NoticeboardHeader onCreatePostClick={createModalHandlers.open}/>

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
        {updatedMessageOpened && (
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t('noticeboard.noticeboard.postUpdated')}
          </Alert>
        )}
        {deletedMessageOpened && (
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t('noticeboard.noticeboard.postDeleted')}
          </Alert>
        )}
        {data && data.posts.length === 0 &&
          <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
            {t('noticeboard.noticeboard.noPosts')}
          </Alert>
        }

        {data && data.posts.length > 0 && (
          <div className={'noticeboard-posts'}>
            {data.posts.map((post) =>
              <NoticeboardPost
                key={post.id}
                post={post}
                onEditClick={() => onEditClick(post)}
                onDeleteClick={() => onDeleteClick(post)}
              />)}
          </div>
        )}
      </div>
    </>
  );
};