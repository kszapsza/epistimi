import './Summary.scss';
import { useAppSelector } from '../../../store/hooks';

export const Summary = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className="summary">
      {user && `your role: ${user.role}`}
    </div>
  );
};
