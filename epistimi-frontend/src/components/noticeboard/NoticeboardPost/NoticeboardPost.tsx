import './NoticeboardPost.scss';
import { Card } from '@mantine/core';
import { NoticeboardPostActions, NoticeboardPostContent, NoticeboardPostHeader } from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';

interface NoticeboardPostProps {
  post: NoticeboardPostResponse;
  onLikeClick: () => void;
  onEditClick: () => void;
  onDeleteClick: () => void;
}

export const NoticeboardPost =
  ({ post, onLikeClick, onEditClick, onDeleteClick }: NoticeboardPostProps,
  ): JSX.Element => {
    return (
      <Card className={'noticeboard-post'} radius={'md'} withBorder>
        <NoticeboardPostHeader post={post}/>
        <NoticeboardPostContent content={post.content}/>
        <NoticeboardPostActions
          postAuthor={post.author}
          onLikeClick={onLikeClick}
          onEditClick={onEditClick}
          onDeleteClick={onDeleteClick}
        />
      </Card>
    );
  };