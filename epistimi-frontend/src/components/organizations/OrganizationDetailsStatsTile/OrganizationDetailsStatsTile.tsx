import './OrganizationDetailsStatsTile.scss';

interface OrganizationDetailsStatsTileProps {
  label: string;
  value: string | number;
}

export const OrganizationDetailsStatsTile = ({ label, value }: OrganizationDetailsStatsTileProps): JSX.Element => {
  return (
    <div className={'organization-stats-tile'}>
      <div className={'organization-stats-label'}>{label}</div>
      <div className={'organization-stats-value'}>{value}</div>
    </div>
  );
};
