import './Footer.scss';
import { useTranslation } from 'react-i18next';

export const Footer = (): JSX.Element => {
  const { t } = useTranslation();
  return (
    <footer className={'footer'}>
      <div className={'footer-logo'}>
        Epistimi
      </div>
      <div className={'footer-columns'}>
        <div className={'footer-column'}>
          <div className={'footer-column-header'}>{t('navigation.footer.company')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.aboutUs')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.careers')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.contact')}</div>
        </div>
        <div className={'footer-column'}>
          <div className={'footer-column-header'}>{t('navigation.footer.offer')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.forSchools')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.forGovernments')}</div>
        </div>
        <div className={'footer-column'}>
          <div className={'footer-column-header'}>{t('navigation.footer.forClient')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.techSupport')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.bugReport')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.termsAndConditions')}</div>
          <div className={'footer-column-item'}>{t('navigation.footer.privacyPolicy')}</div>
        </div>
      </div>
      <div className={'footer-copyright'}>
        &#169; 2022 Epistimi Sp. z&nbsp;o.o.
      </div>
    </footer>
  );
};
