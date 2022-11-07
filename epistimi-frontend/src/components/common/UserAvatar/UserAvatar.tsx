import { Avatar, AvatarProps } from '@mantine/core';
import { MantineColor } from '@mantine/styles';
import { UserResponse, UserRole } from '../../../dto/user';

interface UserAvatarProps extends AvatarProps {
  user: UserResponse;
}

export const UserAvatar = (props: UserAvatarProps): JSX.Element => {
  return (
    <Avatar {...props} color={getUserAvatarColor(props.user.role)}>
      {`${props.user.lastName[0]}${props.user.firstName[0]}`.toUpperCase()}
    </Avatar>
  );
};

export const getUserAvatarColor = (userRole: UserRole): MantineColor => {
  switch (userRole) {
    case UserRole.EPISTIMI_ADMIN:
      return 'red';
    case UserRole.ORGANIZATION_ADMIN:
      return 'orange';
    case UserRole.TEACHER:
      return 'blue';
    case UserRole.STUDENT:
      return 'teal';
    case UserRole.PARENT:
      return 'green';
  }
};
