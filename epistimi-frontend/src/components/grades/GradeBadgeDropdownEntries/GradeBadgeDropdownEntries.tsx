import './GradeBadgeDropdownEntries.scss';
import { GradeBadgeDropdownEntry } from '../GradeBadgeDropdownEntry';
import { GradeResponse } from '../../../dto/grade';
import dayjs from 'dayjs';

interface GradeBadgeDropdownEntriesProps {
  grade: GradeResponse;
}

export const GradeBadgeDropdownEntries = (
  { grade }: GradeBadgeDropdownEntriesProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY, HH:mm';

  return (
    <div className={'grade-dropdown-entries'}>
      <GradeBadgeDropdownEntry
        label={'Waga'}
        value={grade.weight}
      />
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
      <GradeBadgeDropdownEntry
        label={'Licz do średniej'}
        value={grade.countTowardsAverage ? 'TAK' : 'NIE'}
      />
      {grade.comment && (
        <GradeBadgeDropdownEntry
          label={'Komentarz'}
          value={grade.comment}
        />)}
    </div>
  );
};

