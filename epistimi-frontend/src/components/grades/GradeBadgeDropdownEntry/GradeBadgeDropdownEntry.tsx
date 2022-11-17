import './GradeBadgeDropdownEntry.scss';

interface SubjectGradesTeacherGradeDropdownEntryProps {
  label: string;
  value: string | number | JSX.Element;
}

export const GradeBadgeDropdownEntry = (
  { label, value }: SubjectGradesTeacherGradeDropdownEntryProps,
): JSX.Element => {
  return (
    <div className={'grade-dropdown-meta-entry'}>
      <div className={'grade-dropdown-meta-entry-key'}>
        {label}
      </div>
      <div className={'grade-dropdown-meta-entry-value'}>
        {value}
      </div>
    </div>
  );
};