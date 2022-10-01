import './NoticeboardFeedPostHeader.scss';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { Tooltip } from '@mantine/core';
import { useEffect, useState } from 'react';
import { UserAvatar } from '../../common';
import dayjs from 'dayjs';

interface NoticeboardFeedPostHeaderProps {
  post: NoticeboardPostResponse;
}

export const NoticeboardFeedPostHeader = (
  { post: { author, createdDate, title, updatedDate } }: NoticeboardFeedPostHeaderProps,
): JSX.Element => {
  const TOOLTIP_DATE_FORMAT = 'D MMMM YYYY HH:mm';
  const SEPARATOR = ' • ';

  const [dateFromNow, setDateFromNow] = useState<string>('');
  const [dateTooltipLabel, setDateTooltipLabel] = useState<string>('');
  const authorFullName = `${author.firstName} ${author.lastName}`;

  useEffect(() => {
    // TODO: move label to JSON
    if (createdDate !== updatedDate) {
      setDateFromNow(`${dayjs(createdDate).fromNow()} (edytowany)`);
      setDateTooltipLabel(`${dayjs(createdDate).format(TOOLTIP_DATE_FORMAT)}, ` +
        `edytowany: ${dayjs(updatedDate).format(TOOLTIP_DATE_FORMAT)}`);
      return;
    }
    setDateFromNow(`${dayjs(createdDate).fromNow()}`);
    setDateTooltipLabel(dayjs(createdDate).format(TOOLTIP_DATE_FORMAT));
  }, [createdDate, updatedDate]);

  return (
    <div className={'noticeboard-feed-post-header'}>
      <UserAvatar size={'md'} radius={'xl'} user={author}/>
      <div className={'noticeboard-feed-post-meta'}>
        <div className={'noticeboard-feed-post-title'}>
          {title}
        </div>
        <div className={'noticeboard-feed-post-subtitle'}>
            <span className={'noticeboard-feed-post-author'}>
              {authorFullName}
            </span>
          {SEPARATOR}
          <Tooltip label={dateTooltipLabel}>
            <span>{dateFromNow}</span>
          </Tooltip>
        </div>
      </div>
    </div>
  );
};