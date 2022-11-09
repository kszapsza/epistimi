import { Avatar } from '@mantine/core';
import { getUserAvatarColor } from '../UserAvatar/UserAvatar';
import { UserResponse } from '../../../dto/user';

interface UserAvatarGroupProps {
  users: UserResponse[];
  limit: number;
}

export const UserAvatarGroup = ({ users, limit }: UserAvatarGroupProps): JSX.Element => {
  // TODO: sort users by lastName+firstName
  return (
    <Avatar.Group>
      {users
        .slice(0, limit)
        .map((user, idx) =>
          <Avatar radius={'xl'} key={idx} color={getUserAvatarColor(user.role)}>
            {`${user.lastName[0]}${user.firstName[0]}`.toUpperCase()}
          </Avatar>
        )}
      {
        (users.length > limit) && (
          <Avatar radius={'xl'}>
            {`+${users.length - limit}`}
          </Avatar>
        )
      }
    </Avatar.Group>
  );
};
