import './TeachersListingTile.scss';
import { Card } from '@mantine/core';
import { TeacherResponse } from '../../../dto/teacher';
import { UserAvatar } from '../../common';

interface TeachersListingTileProps {
  teacher: TeacherResponse;
}

export const TeachersListingTile = (
  { teacher }: TeachersListingTileProps,
): JSX.Element => {
  return (
    <Card className={'teachers-entry'} component={'a'} href={`/app/teachers/${teacher.id}`}>
      <UserAvatar size={'sm'} radius={'xl'} user={teacher.user}/>
      <div>
        {teacher.user.firstName} {teacher.user.lastName}
      </div>
    </Card>
  );
};
