import './Summary.scss';
import { useAppSelector } from '../../../store/hooks';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className="summary">
      <h2>{user && `Witaj, ${user.firstName}!`}</h2>
    </div>
  );
};
