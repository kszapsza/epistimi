import './NoticeboardFeedPostActions.scss';
import { Button } from '@mantine/core';
import { IconEdit, IconThumbDown, IconThumbUp, IconTrash } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserResponse } from '../../../dto/user';

interface NoticeboardFeedPostActionsProps {
  postAuthor: UserResponse;
}

export const NoticeboardFeedPostActions = ({ postAuthor }: NoticeboardFeedPostActionsProps): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className={'noticeboard-feed-post-actions'}>
      {/* TODO: make buttons work */}
      {/* TODO: move labels to JSONs */}
      <Button.Group>
        <Button leftIcon={<IconThumbUp size={16}/>} size={'xs'} color={'dark'} variant={'default'}>0</Button>
        <Button leftIcon={<IconThumbDown size={16}/>} size={'xs'} color={'dark'} variant={'default'}>0</Button>
      </Button.Group>
      {/* TODO: organization admin can update/delete everything? */}
      {postAuthor.id === user?.id && (
        <Button.Group>
          <Button leftIcon={<IconEdit size={16}/>} size={'xs'} color={'dark'} variant={'default'}>Edytuj</Button>
          <Button leftIcon={<IconTrash size={16}/>} size={'xs'} color={'red'} variant={'default'}>Usuń</Button>
        </Button.Group>
      )}
    </div>
  );
};