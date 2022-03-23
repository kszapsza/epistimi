import './Summary.scss';
import { useAppSelector } from '../../../store/hooks';
import { useEffect } from 'react';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  useEffect(() => {
    document.title = 'Strona główna – Epistimi';
  }, []);

  return (
    <div className="summary">
      <h2>{user && `Witaj, ${user.firstName}!`}</h2>
    </div>
  );
};
