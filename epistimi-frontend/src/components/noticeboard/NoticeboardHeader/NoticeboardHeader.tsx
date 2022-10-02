import './NoticeboardHeader.scss';
import { Button, Title } from '@mantine/core';
import { IconPlus } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';
import { useTranslation } from 'react-i18next';

interface NoticeboardHeaderProps {
  onCreatePostClick: () => void;
}

export const NoticeboardHeader = ({ onCreatePostClick }: NoticeboardHeaderProps): JSX.Element => {
  const { t } = useTranslation();
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className={'noticeboard-header'}>
      <Title order={2}>
        {t('noticeboard.noticeboard.title')}
      </Title>
      {(user?.role === UserRole.ORGANIZATION_ADMIN || user?.role == UserRole.TEACHER) &&
        <Button
          leftIcon={<IconPlus size={16}/>}
          onClick={onCreatePostClick}
          variant={'default'}
        >
          {t('noticeboard.noticeboard.createPost')}
        </Button>}
    </div>
  );
};