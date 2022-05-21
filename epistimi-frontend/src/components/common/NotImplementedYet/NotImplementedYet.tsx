import { Alert } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';

export const NotImplementedYet = (): JSX.Element => {
  return (
    <Alert icon={<IconAlertCircle size={16}/>} color={'orange'}>
      Ten komponent nie został (jeszcze) zaimplementowany.
    </Alert>
  );
};
