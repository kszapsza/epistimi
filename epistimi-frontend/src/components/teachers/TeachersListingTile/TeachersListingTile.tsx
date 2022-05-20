import './TeachersListingTile.scss';
import { Avatar } from '@mantine/core';
import { TeacherResponse } from '../../../dto/teacher';

interface TeachersListingTileProps {
  teacher: TeacherResponse;
}

export const TeachersListingTile = (
  { teacher }: TeachersListingTileProps,
): JSX.Element => {
  return (
    <div className={'teachers-entry'}>
      <Avatar size={'sm'} radius={'xl'} color={'blue'}>
        {teacher.user.firstName[0]}{teacher.user.lastName[0]}
      </Avatar>
      <div>
        {teacher.user.firstName} {teacher.user.lastName}
      </div>
    </div>
  );
};
