import './MenuItem.scss';
import { Group, Navbar, Text, ThemeIcon, UnstyledButton } from '@mantine/core';
import { NavLink } from 'react-router-dom';

interface MenuItemProps {
  icon: JSX.Element;
  label: string;
  href: string;
  onClick: () => void;
}

export const MenuItem = (props: MenuItemProps): JSX.Element => {
  return (
    <Navbar.Section component={NavLink} to={props.href}>
      <UnstyledButton className={'menu-item'} onClick={props.onClick}>
        <Group>
          <ThemeIcon variant={'light'} color={'blue'} size={'md'} className={'menu-item-icon'}>
            {props.icon}
          </ThemeIcon>
          <Text size={'sm'}>
            {props.label}
          </Text>
        </Group>
      </UnstyledButton>
    </Navbar.Section>
  );
};
