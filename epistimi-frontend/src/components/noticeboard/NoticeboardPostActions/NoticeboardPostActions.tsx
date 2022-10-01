import './NoticeboardPostActions.scss';
import { Button } from '@mantine/core';
import { IconEdit, IconThumbUp, IconTrash } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserResponse } from '../../../dto/user';
import { useTranslation } from 'react-i18next';

interface NoticeboardPostActionsProps {
  postAuthor: UserResponse;
  onLikeClick: () => void;
  onEditClick: () => void;
  onDeleteClick: () => void;
}

export const NoticeboardPostActions = (
  { postAuthor, onLikeClick, onEditClick, onDeleteClick }: NoticeboardPostActionsProps,
  ): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { t } = useTranslation();

  return (
    <div className={'noticeboard-post-actions'}>
      {/* TODO: make buttons work */}
      <Button
        disabled
        leftIcon={<IconThumbUp size={16}/>}
        size={'xs'}
        color={'dark'}
        variant={'default'}
        onClick={onLikeClick}
      >
        0
      </Button>
      {/* TODO: organization admin can update/delete everything? */}
      {postAuthor.id === user?.id && (
        <Button.Group>
          <Button
            leftIcon={<IconEdit size={16}/>}
            size={'xs'}
            color={'dark'}
            variant={'default'}
            onClick={onEditClick}
          >
            {t('noticeboard.noticeboardPostActions.edit')}
          </Button>
          <Button
            leftIcon={<IconTrash size={16}/>}
            size={'xs'}
            color={'red'}
            variant={'default'}
            onClick={onDeleteClick}
          >
            {t('noticeboard.noticeboardPostActions.delete')}
          </Button>
        </Button.Group>
      )}
    </div>
  );
};