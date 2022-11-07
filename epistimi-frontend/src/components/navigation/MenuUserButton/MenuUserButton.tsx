import './MenuUserButton.scss';
import { Box, Group, Text, UnstyledButton, UnstyledButtonProps } from '@mantine/core';
import { forwardRef } from 'react';
import { IconChevronRight } from '@tabler/icons';
import { useAppSelector } from '../../../store/hooks';
import { UserAvatar } from '../../common';

type MenuUserButtonProps = UnstyledButtonProps;

export const MenuUserButton = forwardRef<HTMLButtonElement, MenuUserButtonProps>((props, ref) => {
  const { user } = useAppSelector((state) => state.auth);

  if (!user) {
    return <></>;
  }

  return (
    <div className={'menu-user-box'}>
      <UnstyledButton className={'menu-user-btn'} ref={ref} {...props}>
        <Group>
          <UserAvatar radius={'xl'} user={user}/>
          <Box sx={{ flex: 1 }}>
            <Text size="sm" weight={500}>
              {user.lastName} {user.firstName}
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
