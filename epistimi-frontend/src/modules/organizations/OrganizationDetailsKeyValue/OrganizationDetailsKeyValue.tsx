import './OrganizationDetailsKeyValue.scss';

interface OrganizationDetailsKeyValueProps {
  label: string | JSX.Element;
  value: string | JSX.Element;
}

export const OrganizationDetailsKeyValue = ({ label, value }: OrganizationDetailsKeyValueProps): JSX.Element => {
  return (
    <div className={'organization-entry'}>
      <div className={'organization-key'}>
        {label}
      </div>
      <div className={'organization-value'}>
        {value}
      </div>
    </div>
  );
};
