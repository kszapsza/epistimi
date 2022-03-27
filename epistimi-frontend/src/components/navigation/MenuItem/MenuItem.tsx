import './MenuItem.scss';
import { Group, Navbar, Text, ThemeIcon, UnstyledButton } from '@mantine/core';
import { Link, useMatch } from 'react-router-dom';

interface MenuItemProps {
  icon: JSX.Element;
  label: string;
  href: string;
  onClick: () => void;
}

export const MenuItem = (props: MenuItemProps): JSX.Element => {
  const match = useMatch(props.href);

  return (
    <Navbar.Section component={Link} to={props.href}>
      <UnstyledButton className={'menu-item'} onClick={props.onClick}>
        <Group>
          <ThemeIcon variant={'light'} color={'blue'} size={'md'} className={'menu-item-icon'}>
            {props.icon}
          </ThemeIcon>
          <Text size={'sm'} style={match ? { fontWeight: '600' } : {}}>
            {props.label}
          </Text>
        </Group>
      </UnstyledButton>
    </Navbar.Section>
  );
};
