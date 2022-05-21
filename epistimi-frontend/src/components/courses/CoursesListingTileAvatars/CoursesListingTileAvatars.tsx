import { IconUsers } from '@tabler/icons';
import { StudentResponse } from '../../../dto/student';
import { ThemeIcon } from '@mantine/core';
import { UserAvatarGroup } from '../../common/UserAvatarGroup';

interface CourseListingTileAvatarsProps {
  students: StudentResponse[];
}

export const CoursesListingTileAvatars = (
  { students }: CourseListingTileAvatarsProps,
): JSX.Element => {

  if (students.length === 0) {
    return (
      <ThemeIcon variant={'light'} color={'blue'} size={'lg'}>
        <IconUsers/>
      </ThemeIcon>
    );
  }

  return (
    <UserAvatarGroup limit={3} users={students.map((student) => student.user)}/>
  );
};
