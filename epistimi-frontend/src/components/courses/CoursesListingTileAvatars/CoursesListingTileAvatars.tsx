import { IconUsers } from '@tabler/icons';
import { StudentResponse } from '../../../dto/student';
import { ThemeIcon } from '@mantine/core';
import { UserAvatarGroup } from '../../common';

interface CourseListingTileAvatarsProps {
  students: StudentResponse[];
}

export const CoursesListingTileAvatars = (
  { students }: CourseListingTileAvatarsProps,
): JSX.Element => {

  if (students.length === 0) {
    return (
      <ThemeIcon variant={'light'} color={'blue'} size={'lg'} radius={'xl'}>
        <IconUsers size={18}/>
      </ThemeIcon>
    );
  }

  return (
    <UserAvatarGroup
      limit={3}
      users={students.map((student) => student.user)}
    />
  );
};
