import './Noticeboard.scss';
import { Alert, Button, Modal, Title } from '@mantine/core';
import { IconAlertCircle, IconCheck, IconPlus } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { NoticeboardPost, NoticeboardPostForm, NoticeboardPostFormVariant } from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useDisclosure } from '@mantine/hooks';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const Noticeboard = (): JSX.Element => {
  const { t } = useTranslation();
  const { data, error, loading, reload } = useFetch<NoticeboardPostResponse>('/api/noticeboard/post');

  const [createModalOpened, createModalHandlers] = useDisclosure(false);
  const [createdMessageOpened, createdMessageHandlers] = useDisclosure(false);

  useDocumentTitle(t('noticeboard.noticeboard.title'));

  const onNoticeboardPostCreated = (): void => {
    reload();
    createModalHandlers.close();
    createdMessageHandlers.open();
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
        <div className={'noticeboard-header'}>
          <Title order={2}>{t('noticeboard.noticeboard.title')}</Title>
          <Button
            leftIcon={<IconPlus size={16}/>}
            onClick={createModalHandlers.open}
            variant={'default'}
          >
            {t('noticeboard.noticeboard.createPost')}
          </Button>
        </div>

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

        {data && (
          <div className={'noticeboard-posts'}>
            {data.posts.map((post) =>
              <NoticeboardPost key={post.id} post={post}/>)}
          </div>
        )}
      </div>
    </>
  );
};