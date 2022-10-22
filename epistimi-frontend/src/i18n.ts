import { initReactI18next } from 'react-i18next';
import backend from 'i18next-http-backend';
import i18n from 'i18next';

i18n
  .use(backend)
  .use(initReactI18next)
  .init({
    backend: {
      loadPath: `${process.env.PUBLIC_URL}/locales/{{lng}}/{{ns}}.json`,
    },
    interpolation: {
      escapeValue: false,
    },
    lng: 'pl',
  })
  .then(() => {
    // noop
  });

export default i18n;
