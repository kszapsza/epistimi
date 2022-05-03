import { Avatar, AvatarsGroup, ThemeIcon } from '@mantine/core';
import { StudentResponse } from '../../../dto/student';
import { Users } from 'tabler-icons-react';

interface CourseListingTileAvatarsProps {
  students: StudentResponse[];
}

export const CoursesListingTileAvatars = (
  { students }: CourseListingTileAvatarsProps,
): JSX.Element => {

  if (students.length === 0) {
    return (
      <ThemeIcon variant={'light'} color={'blue'} size={'lg'}>
        <Users/>
      </ThemeIcon>
    );
  }

  return (
    <AvatarsGroup limit={3}>
      {students
        .map((student) => student.user)
        .map((user) => `${user.firstName[0]}${user.lastName[0]}`)
        .map((initials) => initials.toLocaleUpperCase('pl-PL'))
        .map((initials, idx) => <Avatar key={idx} color={'orange'}>{initials}</Avatar>)}
    </AvatarsGroup>
  );
};