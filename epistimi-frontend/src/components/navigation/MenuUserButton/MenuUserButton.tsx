import './MenuUserButton.scss';
import { Avatar, Box, Group, Text, UnstyledButton, UnstyledButtonProps } from '@mantine/core';
import { forwardRef } from 'react';
import { IconChevronRight } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';

type MenuUserButtonProps = UnstyledButtonProps<'button'>;

export const MenuUserButton = forwardRef<HTMLButtonElement, MenuUserButtonProps>((props, ref) => {
  const { user } = useAppSelector((state) => state.auth);

  if (!user) {
    return <></>;
  }

  const initials = `${user.firstName[0]}${user.lastName[0]}`
    .toLocaleUpperCase('pl-PL');

  return (
    <div className={'menu-user-box'}>
      <UnstyledButton className={'menu-user-btn'} ref={ref} {...props}>
        <Group>
          <Avatar radius={'xl'} color={'orange'}>
            {initials}
          </Avatar>
          <Box sx={{ flex: 1 }}>
            <Text size="sm" weight={500}>
              {user.firstName} {user.lastName}
            </Text>
            <Text color="dimmed" size="xs">
              {user.username}
            </Text>
          </Box>
          <IconChevronRight size={18}/>
        </Group>
      </UnstyledButton>
    </div>
  );
});
