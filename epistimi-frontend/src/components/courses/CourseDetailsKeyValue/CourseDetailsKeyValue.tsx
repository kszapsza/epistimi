import './CourseDetailsKeyValue.scss';

interface CourseDetailsKeyValueProps {
  label: string | JSX.Element;
  value: string | JSX.Element;
}

export const CourseDetailsKeyValue = ({ label, value }: CourseDetailsKeyValueProps): JSX.Element => {
  return (
    <div className={'course-entry'}>
      <div className={'course-key'}>
        {label}
      </div>
      <div className={'course-value'}>
        {value}
      </div>
    </div>
  );
};
