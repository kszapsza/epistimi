import './Summary.scss';
import { Title } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { useDocumentTitle } from '../../../hooks';
import { useTranslation } from 'react-i18next';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { t } = useTranslation();

  useDocumentTitle('Strona główna');

  return (
    <div className="summary">
      <Title order={2}>
        {user && t('summary.greeting', { name: user.firstName })}
      </Title>
    </div>
  );
};
