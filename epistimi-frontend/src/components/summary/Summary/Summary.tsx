import './Summary.scss';
import { Title } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { useDocumentTitle } from '../../../hooks';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  useDocumentTitle('Strona główna');

  return (
    <div className="summary">
      <Title order={2}>{user && `Witaj, ${user.firstName}!`}</Title>
    </div>
  );
};
