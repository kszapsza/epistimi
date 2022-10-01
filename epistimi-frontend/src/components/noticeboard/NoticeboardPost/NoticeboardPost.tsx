import './NoticeboardPost.scss';
import { Card } from '@mantine/core';
import { NoticeboardPostActions, NoticeboardPostContent, NoticeboardPostHeader } from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';

interface NoticeboardPostProps {
  post: NoticeboardPostResponse;
}

export const NoticeboardPost = ({ post }: NoticeboardPostProps): JSX.Element => {
  return (
    <Card className={'noticeboard-post'} radius={'md'} withBorder>
      <NoticeboardPostHeader post={post}/>
      <NoticeboardPostContent content={post.content}/>
      <NoticeboardPostActions postAuthor={post.author}/>
    </Card>
  );
};