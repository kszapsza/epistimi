import './NotFound.scss';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export const NotFound = (): JSX.Element => {
  const navigate = useNavigate();
  const { t } = useTranslation();

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
        {t('navigation.notFound.desc')}
      </div>
    </div>
  );
};
