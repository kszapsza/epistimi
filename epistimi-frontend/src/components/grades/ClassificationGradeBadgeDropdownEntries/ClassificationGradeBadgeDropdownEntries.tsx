import './ClassificationGradeBadgeDropdownEntries.scss';
import { GradeBadgeDropdownEntry } from '../GradeBadgeDropdownEntry';
import { StudentSubjectClassificationGradeResponse } from '../../../dto/student-grades';
import { SubjectStudentClassificationGradeResponse } from '../../../dto/subject-grades';
import dayjs from 'dayjs';

interface ClassificationGradeBadgeDropdownEntriesProps {
  grade: SubjectStudentClassificationGradeResponse | StudentSubjectClassificationGradeResponse;
}

export const ClassificationGradeBadgeDropdownEntries = (
  { grade }: ClassificationGradeBadgeDropdownEntriesProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY, HH:mm';

  return (
    <div className={'grade-dropdown-entries'}>
      <GradeBadgeDropdownEntry
        label={'Nauczyciel'}
        value={`${grade.issuedBy.academicTitle || ''} ${grade.issuedBy.lastName} ${grade.issuedBy.firstName}`.trim()}
      />
      <GradeBadgeDropdownEntry
        label={'Wystawiono'}
        value={dayjs(grade.issuedAt).format(DATE_FORMAT)}
      />
      {grade.updatedAt && (
        <GradeBadgeDropdownEntry
          label={'Zaktualizowano'}
          value={dayjs(grade.updatedAt).format(DATE_FORMAT)}
        />)}
    </div>
  );
};

