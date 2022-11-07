import { Avatar, AvatarProps } from '@mantine/core';

interface SubjectAvatarProps extends AvatarProps {
  subjectName: string;
}

export const SubjectAvatar = ({ subjectName, ...props }: SubjectAvatarProps): JSX.Element => {
  return (
    <Avatar {...props} color={'gray'}>
      {getSubjectInitials(subjectName)}
    </Avatar>
  );
};

const getSubjectInitials = (name: string): string => {
  return name.split(' ')
    .map((word) => word[0])
    .slice(0, 3)
    .join("")
    .toUpperCase();
};
