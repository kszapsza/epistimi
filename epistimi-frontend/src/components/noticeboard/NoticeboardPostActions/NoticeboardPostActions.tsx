import './NoticeboardPostActions.scss';
import { Button } from '@mantine/core';
import { IconEdit, IconTrash } from '@tabler/icons';
import { useTranslation } from 'react-i18next';

interface NoticeboardPostActionsProps {
  onEditClick: () => void;
  onDeleteClick: () => void;
}

export const NoticeboardPostActions = (
  { onEditClick, onDeleteClick }: NoticeboardPostActionsProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <div className={'noticeboard-post-actions'}>
      {/* TODO: organization admin can update/delete everything? */}
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
    </div>
  );
};