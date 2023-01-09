import './OrganizationsListingTile.scss';
import { Card } from '@mantine/core';

interface OrganizationsListingTileProps {
  id: string;
  name: string;
  admin: string;
}

export const OrganizationsListingTile = (
  props: OrganizationsListingTileProps,
): JSX.Element => {
  return (
    <Card className={'organizations-tile'} role={'row'}
          component={'a'} href={`/app/organizations/${props.id}`}>
      <div className={'organizations-tile-meta'}>
        <div className={'organizations-tile-title'}>
          {props.name}
        </div>
        <div className={'organizations-tile-subtitle'}>
          <samp>{props.id}</samp> ∙ {props.admin}
        </div>
      </div>
    </Card>
  );
};
