import './NoticeboardFeedPost.scss';
import { Card } from '@mantine/core';
import {
  NoticeboardFeedPostActions,
  NoticeboardFeedPostContent,
  NoticeboardFeedPostHeader,
} from '../../noticeboard';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';

interface NoticeboardFeedPostProps {
  post: NoticeboardPostResponse;
}

export const NoticeboardFeedPost = ({ post }: NoticeboardFeedPostProps): JSX.Element => {
  return (
    <Card className={'noticeboard-feed-post'} radius={'md'} withBorder>
      <NoticeboardFeedPostHeader post={post}/>
      <NoticeboardFeedPostContent content={post.content}/>
      <NoticeboardFeedPostActions postAuthor={post.author}/>
    </Card>
  );
};