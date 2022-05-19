import './TeachersListingTile.scss';
import { TeacherResponse } from '../../../dto/teacher';

interface TeachersListingTileProps {
  teacher: TeacherResponse;
}

export const TeachersListingTile = (
  { teacher }: TeachersListingTileProps,
): JSX.Element => {
  return (
    <div className={'teachers-entry'}>
      {teacher.user.firstName} {teacher.user.lastName}
    </div>
  );
};
