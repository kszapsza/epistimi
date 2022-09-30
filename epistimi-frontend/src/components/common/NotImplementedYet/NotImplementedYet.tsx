import { Alert } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { useTranslation } from 'react-i18next';

export const NotImplementedYet = (): JSX.Element => {
  const { t } = useTranslation();
  return (
    <Alert icon={<IconAlertCircle size={16}/>} color={'orange'}>
      {t('common.notImplementedYet')}
    </Alert>
  );
};
