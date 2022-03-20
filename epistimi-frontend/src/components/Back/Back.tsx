import './Back.scss';
import { ArrowBack } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

export const Back = (): JSX.Element => {
  const navigate = useNavigate();

  return (
    <div className={'back'} onClick={() => navigate('./..')}>
      <ArrowBack fontSize={'medium'} />
    </div>
  );
};