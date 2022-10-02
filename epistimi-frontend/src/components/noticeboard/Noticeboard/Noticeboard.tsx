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
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useReducer } from 'react';
import { useTranslation } from 'react-i18next';

interface NoticeboardState {
  createModalOpened: boolean;
  deletedPost: NoticeboardPostResponse | null;
  editedPost: NoticeboardPostResponse | null;
  successMessageTranslationKey: string | null;
}

type NoticeboardAction =
  | { type: 'OPEN_POST_CREATE_MODAL' }
  | { type: 'CLOSE_POST_CREATE_MODAL' }
  | { type: 'ON_POST_CREATED' }
  | { type: 'OPEN_POST_EDIT_MODAL', editedPost: NoticeboardPostResponse }
  | { type: 'CLOSE_POST_EDIT_MODAL' }
  | { type: 'ON_POST_EDITED' }
  | { type: 'OPEN_POST_DELETE_MODAL', deletedPost: NoticeboardPostResponse }
  | { type: 'CLOSE_POST_DELETE_MODAL' }
  | { type: 'ON_POST_DELETED' };

const noticeboardReducer = (state: NoticeboardState, action: NoticeboardAction): NoticeboardState => {
  switch (action.type) {
    case 'OPEN_POST_CREATE_MODAL':
      return {
        ...state,
        createModalOpened: true,
      };
    case 'CLOSE_POST_CREATE_MODAL':
      return {
        ...state,
        createModalOpened: false,
      };
    case 'ON_POST_CREATED':
      return {
        ...state,
        createModalOpened: false,
        successMessageTranslationKey: 'noticeboard.noticeboard.newPostCreated',
      };
    case 'OPEN_POST_EDIT_MODAL':
      return {
        ...state,
        editedPost: action.editedPost,
      };
    case 'CLOSE_POST_EDIT_MODAL':
      return {
        ...state,
        editedPost: null,
      };
    case 'ON_POST_EDITED':
      return {
        ...state,
        editedPost: null,
        successMessageTranslationKey: 'noticeboard.noticeboard.postUpdated',
      };
    case 'OPEN_POST_DELETE_MODAL':
      return {
        ...state,
        deletedPost: action.deletedPost,
      };
    case 'CLOSE_POST_DELETE_MODAL':
      return {
        ...state,
        deletedPost: null,
      };
    case 'ON_POST_DELETED':
      return {
        ...state,
        deletedPost: null,
        successMessageTranslationKey: 'noticeboard.noticeboard.postDeleted',
      };
  }
};

export const Noticeboard = (): JSX.Element => {
  const { t } = useTranslation();
  const { data, error, loading, reload } = useFetch<NoticeboardPostResponse>('/api/noticeboard/post');

  const [{
    createModalOpened,
    deletedPost,
    editedPost,
    successMessageTranslationKey,
  }, dispatch] = useReducer(noticeboardReducer, {
    createModalOpened: false,
    deletedPost: null,
    editedPost: null,
    successMessageTranslationKey: null,
  });

  useDocumentTitle(t('noticeboard.noticeboard.title'));

  return (
    <>
      <Modal
        onClose={() => dispatch({ type: 'CLOSE_POST_CREATE_MODAL' })}
        opened={createModalOpened}
        size={'lg'}
        title={t('noticeboard.noticeboard.createNewPostModalTitle')}
      >
        <NoticeboardPostForm
          variant={NoticeboardPostFormVariant.CREATE}
          onSubmit={() => {
            reload();
            dispatch({ type: 'ON_POST_CREATED' });
          }}
          onCancel={() => dispatch({ type: 'CLOSE_POST_CREATE_MODAL' })}
        />
      </Modal>

      {deletedPost && <Modal
        onClose={() => dispatch({ type: 'ON_POST_CREATED' })}
        opened={!!deletedPost}
        size={'lg'}
        title={t('noticeboard.noticeboard.deletePostModalTitle')}
      >
        <NoticeboardPostDeleteConfirmation
          post={deletedPost}
          afterDelete={() => {
            reload();
            dispatch({ type: 'ON_POST_DELETED' });
          }}
          onCancel={() => dispatch({ type: 'CLOSE_POST_DELETE_MODAL' })}
        />
      </Modal>}

      {editedPost && <Modal
        onClose={() => dispatch({ type: 'CLOSE_POST_EDIT_MODAL' })}
        opened={!!editedPost}
        size={'lg'}
        title={t('noticeboard.noticeboard.editPostModalTitle')}
      >
        <NoticeboardPostForm
          variant={NoticeboardPostFormVariant.UPDATE}
          onSubmit={() => {
            reload();
            dispatch({ type: 'ON_POST_EDITED' });
          }}
          onCancel={() => dispatch({ type: 'CLOSE_POST_EDIT_MODAL' })}
          post={editedPost}
        />
      </Modal>}

      <div className={'noticeboard'}>
        <NoticeboardHeader
          onCreatePostClick={() => dispatch({ type: 'OPEN_POST_CREATE_MODAL' })}
        />

        {error && (
          <Alert icon={<IconAlertCircle size={16}/>} color={'red'}>
            {t('noticeboard.noticeboard.couldNotLoadPosts')}
          </Alert>
        )}
        {successMessageTranslationKey && (
          <Alert icon={<IconCheck size={16}/>} color={'green'}>
            {t(successMessageTranslationKey)}
          </Alert>
        )}
        {data && data.posts.length === 0 &&
          <Alert icon={<IconInfoCircle size={16}/>} color={'blue'}>
            {t('noticeboard.noticeboard.noPosts')}
          </Alert>
        }

        {loading && <LoaderBox/>}

        {data && data.posts.length > 0 && (
          <div className={'noticeboard-posts'}>
            {data.posts.map((post) =>
              <NoticeboardPost
                key={post.id}
                post={post}
                onEditClick={() => dispatch({ type: 'OPEN_POST_EDIT_MODAL', editedPost: post })}
                onDeleteClick={() => dispatch({ type: 'OPEN_POST_DELETE_MODAL', deletedPost: post })}
              />)}
          </div>
        )}
      </div>
    </>
  );
};