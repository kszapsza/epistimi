import './OrganizationColorStatus.scss';
import { OrganizationStatus } from '../../../dto/organization';

interface OrganizationColorStatusProps {
  status: OrganizationStatus;
}

export const OrganizationColorStatus = ({ status }: OrganizationColorStatusProps): JSX.Element => {
  switch (status) {
    case OrganizationStatus.ENABLED:
      return <div className={'organization-status-enabled'}>AKTYWNA</div>;
    case OrganizationStatus.DISABLED:
      return <div className={'organization-status-disabled'}>NIEAKTYWNA</div>;
  }
};
