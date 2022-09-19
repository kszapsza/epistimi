import './CourseAddStudentParentsList.scss';
import { IconTrash } from '@tabler/icons';
import { StudentRegisterFormData } from '../CourseAddStudent';
import { Title } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface CourseAddStudentParentsListProps {
  parents: StudentRegisterFormData[];
  removeCallback: (idx: number) => void;
}

export const CourseAddStudentParentsList = (
  { parents, removeCallback }: CourseAddStudentParentsListProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <div className={'add-student-parent-list'}>
      <Title order={4}>
        {t('courses.courseAddStudentParentsList.studentsParents')}
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
