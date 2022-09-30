import { Avatar, AvatarsGroup, AvatarsGroupProps } from '@mantine/core';
import { getUserAvatarColor } from '../UserAvatar/UserAvatar';
import { UserResponse } from '../../../dto/user';

interface UserAvatarGroupProps extends AvatarsGroupProps {
  users: UserResponse[];
}

export const UserAvatarGroup = (props: UserAvatarGroupProps): JSX.Element => {
  return (
    <AvatarsGroup {...props}>
      {props.users
        .map((user, idx) =>
          <Avatar key={idx} color={getUserAvatarColor(user.role)}>
            {`${user.firstName[0]}${user.lastName[0]}`.toUpperCase()}
          </Avatar>
        )}
    </AvatarsGroup>
  );
};
