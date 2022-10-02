import './NoticeboardPostDeleteConfirmation.scss';
import { Button } from '@mantine/core';
import { IconTrash } from '@tabler/icons';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useTranslation } from 'react-i18next';
import axios from 'axios';

interface NoticeboardPostDeleteConfirmationProps {
  post: NoticeboardPostResponse;
  afterDelete: () => void;
  onCancel: () => void;
}

export const NoticeboardPostDeleteConfirmation = (
  { post, afterDelete, onCancel }: NoticeboardPostDeleteConfirmationProps,
): JSX.Element => {

  const { t }= useTranslation();

  const onDeleteClick = (): void => {
    axios.delete(`/api/noticeboard/post/${post.id}`)
      .then(() => {
        afterDelete();
      });
  };

  return (
    <div className={'noticeboard-post-delete-confirmation'}>
      <div className={'noticeboard-post-delete-disclaimer'}>
        {t('noticeboard.noticeboardPostDeleteConfirmation.areYouSure', { title: post.title })}
      </div>
      <div className={'noticeboard-post-delete-actions'}>
        <Button
          leftIcon={<IconTrash size={16}/>}
          onClick={onDeleteClick}
          color={'red'}
          variant={'filled'}
        >
          {t('noticeboard.noticeboardPostDeleteConfirmation.delete')}
        </Button>
        <Button
          onClick={onCancel}
          variant={'default'}
        >
          {t('noticeboard.noticeboardPostDeleteConfirmation.cancel')}
        </Button>
      </div>
    </div>
  );
};