import './SubjectGradesTeacherGradeDropdown.scss';
import { ActionIcon, Badge } from '@mantine/core';
import { determineTextColor } from '../../../utils/color-utils';
import { GradeResponse } from '../../../dto/grade';
import { IconPencil, IconTrash } from '@tabler/icons';
import { SubjectGradesTeacherGradeDropdownEntry } from '../SubjectGradesTeacherGradeDropdownEntry';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';
import dayjs from 'dayjs';

interface SubjectGradesTeacherGradeDropdownProps {
  grade: GradeResponse;
  showActions?: boolean;
}

export const SubjectGradesTeacherGradeDropdown = (
  { grade, showActions }: SubjectGradesTeacherGradeDropdownProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY, HH:mm';
  const { user } = useAppSelector((state) => state.auth);

  return (
    <div className={'subject-grade-dropdown'}>
      <div className={'subject-grade-dropdown-head'}>
        <Badge
          radius={'xs'} size={'xl'} p={0}
          style={{
            backgroundColor: grade.category.color ?? '#228be6',
            color: grade.category.color ? determineTextColor(grade.category.color) : '#fff',
            fontWeight: '600',
            width: '36px',
            height: '36px',
          }}
        >
          {grade.value.displayName}
        </Badge>
        <div className={'subject-grade-dropdown-head-text'}>
          <div className={'subject-grade-dropdown-head-category'}>
            {grade.category.name}
          </div>
          <div className={'subject-grade-dropdown-head-value'}>
            {grade.value.fullName}
          </div>
        </div>
        {user && user.role == UserRole.TEACHER && showActions !== false && (
          <div className={'subject-grade-dropdown-head-actions'}>
            <ActionIcon size={24}>
              <IconPencil size={16}/>
            </ActionIcon>
            <ActionIcon size={24} color={'red'}>
              <IconTrash size={16}/>
            </ActionIcon>
          </div>)}
      </div>

      <div className={'subject-grade-dropdown-meta'}>
        <SubjectGradesTeacherGradeDropdownEntry
          label={'Waga'}
          value={grade.weight}
        />
        <SubjectGradesTeacherGradeDropdownEntry
          label={'Nauczyciel'}
          value={`${grade.issuedBy.academicTitle || ''} ${grade.issuedBy.lastName} ${grade.issuedBy.firstName}`.trim()}
        />
        <SubjectGradesTeacherGradeDropdownEntry
          label={'Wystawiono'}
          value={dayjs(grade.issuedAt).format(DATE_FORMAT)}
        />
        {grade.updatedAt && (
          <SubjectGradesTeacherGradeDropdownEntry
            label={'Zaktualizowano'}
            value={dayjs(grade.updatedAt).format(DATE_FORMAT)}
          />)}
        <SubjectGradesTeacherGradeDropdownEntry
          label={'Licz do średniej'}
          value={grade.countTowardsAverage ? 'TAK' : 'NIE'}
        />
        {grade.comment && (
          <SubjectGradesTeacherGradeDropdownEntry
            label={'Komentarz'}
            value={grade.comment}
          />)}
      </div>
    </div>
  );
};
