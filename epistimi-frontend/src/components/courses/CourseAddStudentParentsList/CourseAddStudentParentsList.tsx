import './CourseAddStudentParentsList.scss';
import { IconTrash } from '@tabler/icons';
import { StudentRegisterFormData } from '../CourseAddStudent';
import { Title } from '@mantine/core';

interface CourseAddStudentParentsListProps {
  parents: StudentRegisterFormData[];
  removeCallback: (idx: number) => void;
}

export const CourseAddStudentParentsList = (
  { parents, removeCallback }: CourseAddStudentParentsListProps,
): JSX.Element => {
  return (
    <div className={'add-student-parent-list'}>
      <Title order={4}>
        Rodzice ucznia
      </Title>
      {parents.map((parent, idx) => (
        <div className={'add-student-parent-entry'} key={idx}>
          <div className={'add-student-parent-name'}>
            {parent.firstName} {parent.lastName}
          </div>
          <div className={'add-student-parent-actions'}>
            <IconTrash size={18} onClick={() => removeCallback(idx)}/>
          </div>
        </div>
      ))}
    </div>
  );
};
