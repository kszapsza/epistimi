import { Title } from '@mantine/core';
import { useDocumentTitle } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const NoticeboardFeed = (): JSX.Element => {
  const { t } = useTranslation();

  useDocumentTitle(t('noticeboard.noticeboardFeed.title'));

  return (
    <div className={'noticeboard'}>
      <div className={'noticeboard-header'}>
        <Title order={2}>{t('noticeboard.noticeboardFeed.title')}</Title>
      </div>


    </div>
  );
};