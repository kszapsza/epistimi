import './NoticeboardPost.scss';
import { Card } from '@mantine/core';
import { NoticeboardPostActions, NoticeboardPostContent, NoticeboardPostHeader } from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useAppSelector } from '../../../store/hooks';

interface NoticeboardPostProps {
  post: NoticeboardPostResponse;
  onEditClick: () => void;
  onDeleteClick: () => void;
}

export const NoticeboardPost =
  ({ post, onEditClick, onDeleteClick }: NoticeboardPostProps,
  ): JSX.Element => {
    const { user } = useAppSelector((state) => state.auth);

    return (
      <Card className={'noticeboard-post'} radius={'md'} withBorder>
        <NoticeboardPostHeader post={post}/>
        <NoticeboardPostContent content={post.content}/>
        {post.author.id === user?.id && (<NoticeboardPostActions
          onEditClick={onEditClick}
          onDeleteClick={onDeleteClick}
        />)}
      </Card>
    );
  };