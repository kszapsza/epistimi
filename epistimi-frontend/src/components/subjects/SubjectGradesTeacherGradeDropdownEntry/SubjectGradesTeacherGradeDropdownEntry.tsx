import './SubjectGradesTeacherGradeDropdownEntry.scss';

interface SubjectGradesTeacherGradeDropdownEntryProps {
  label: string;
  value: string | number | JSX.Element;
}

export const SubjectGradesTeacherGradeDropdownEntry = (
  { label, value }: SubjectGradesTeacherGradeDropdownEntryProps,
): JSX.Element => {
  return (
    <div className={'subject-grade-dropdown-meta-entry'}>
      <div className={'subject-grade-dropdown-meta-entry-key'}>
        {label}
      </div>
      <div className={'subject-grade-dropdown-meta-entry-value'}>
        {value}
      </div>
    </div>
  );
};