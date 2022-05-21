import './OrganizationsListingTile.scss';
import { Card } from '@mantine/core';
import { Link } from 'react-router-dom';
import { OrganizationColorStatus } from '../OrganizationColorStatus';
import { OrganizationStatus } from '../../../dto/organization';

interface OrganizationsListingTileProps {
  id: string;
  name: string;
  admin: string;
  status: OrganizationStatus;
}

export const OrganizationsListingTile = (
  props: OrganizationsListingTileProps,
): JSX.Element => {
  return (
    <Card className={'organizations-tile'} role={'row'}
          component={'a'} href={`/app/organizations/${props.id}`}>
      <div className={'organizations-tile-meta'}>
        <div className={'organizations-tile-title'}>
          <Link to={`./${props.id}`}>{props.name}</Link>
        </div>
        <div className={'organizations-tile-subtitle'}>
          <samp>{props.id}</samp> ∙ {props.admin}
        </div>
      </div>
      <div className={'organizations-tile-status'}>
        <OrganizationColorStatus status={props.status}/>
      </div>
    </Card>
  );
};
