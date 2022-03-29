import './NotFound.scss';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const NotFound = (): JSX.Element => {
  const navigate = useNavigate();

  useEffect(() => {
    setTimeout(() => {
      navigate('/');
    }, 1000);
  });

  return (
    <div className={"not-found"}>
      <h1 className={"not-found-code"}>
        404
      </h1>
      <div className={"not-found-desc"}>
        Strona o&nbsp;podanym adresie nie istnieje
      </div>
    </div>
  );
};
