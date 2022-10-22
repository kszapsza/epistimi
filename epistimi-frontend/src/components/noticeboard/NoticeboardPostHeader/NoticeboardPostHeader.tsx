import './NoticeboardPostHeader.scss';
import { NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { Tooltip } from '@mantine/core';
import { useEffect, useState } from 'react';
import { UserAvatar } from '../../common';
import { useTranslation } from 'react-i18next';
import dayjs from 'dayjs';

interface NoticeboardPostHeaderProps {
  post: NoticeboardPostResponse;
}

export const NoticeboardPostHeader = (
  { post: { author, createdDate, title, updatedDate } }: NoticeboardPostHeaderProps,
): JSX.Element => {
  const TOOLTIP_DATE_FORMAT = 'D MMMM YYYY HH:mm';
  const SEPARATOR = ' • ';

  const { t } = useTranslation();

  const [dateFromNow, setDateFromNow] = useState<string>('');
  const [dateTooltipLabel, setDateTooltipLabel] = useState<string>('');
  const authorFullName = `${author.firstName} ${author.lastName}`;

  useEffect(() => {
    if (createdDate !== updatedDate) {
      setDateFromNow(`${dayjs(createdDate).fromNow()} (${t('noticeboard.noticeboardPostHeader.edited')})`);
      setDateTooltipLabel(`${dayjs(createdDate).format(TOOLTIP_DATE_FORMAT)}, ` +
        `${t('noticeboard.noticeboardPostHeader.edited')}: ${dayjs(updatedDate).format(TOOLTIP_DATE_FORMAT)}`);
      return;
    }
    setDateFromNow(`${dayjs(createdDate).fromNow()}`);
    setDateTooltipLabel(dayjs(createdDate).format(TOOLTIP_DATE_FORMAT));
  }, [createdDate, updatedDate]);

  return (
    <div className={'noticeboard-post-header'}>
      <UserAvatar size={'md'} radius={'xl'} user={author}/>
      <div className={'noticeboard-post-meta'}>
        <div className={'noticeboard-post-title'}>
          {title}
        </div>
        <div className={'noticeboard-post-subtitle'}>
            <span className={'noticeboard-post-author'}>
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