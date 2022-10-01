import './NoticeboardPostContent.scss';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';

interface NoticeboardPostContentProps {
  content: string;
}

const CONTENT_PREVIEW_WORD_LIMIT = 70;

export const NoticeboardPostContent = (
  { content }: NoticeboardPostContentProps,
): JSX.Element => {

  const { t } = useTranslation();

  const [shortenedContent, setShortenedContent] = useState<string | null>(null);
  const [isContentExpanded, setContentExpanded] = useState<boolean>(false);

  useEffect(() => {
    const words = content.split(' ');
    if (words.length <= CONTENT_PREVIEW_WORD_LIMIT) {
      setShortenedContent(null);
      return;
    }
    setShortenedContent(
      words.splice(0, CONTENT_PREVIEW_WORD_LIMIT)
        .reduce((previous, current) => previous + ' ' + current)
        .concat('…')
    );
  }, [content]);

  return (
    <div className={'noticeboard-post-content'}>
      {
        (shortenedContent === null || isContentExpanded)
          ? content
          : shortenedContent
      }
      {
        shortenedContent &&
        <a className={'noticeboard-post-expand'} onClick={() => setContentExpanded(!isContentExpanded)}>
          {isContentExpanded
            ? ` ${t('noticeboard.noticeboardPostContent.showLess')}`
            : ` ${t('noticeboard.noticeboardPostContent.showMore')}`}
        </a>
      }
    </div>
  );
};