import './Summary.scss';
import { Title } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { useEffect } from 'react';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  useEffect(() => {
    document.title = 'Strona główna – Epistimi';
  }, []);

  return (
    <div className="summary">
      <Title order={2}>{user && `Witaj, ${user.firstName}!`}</Title>
    </div>
  );
};
