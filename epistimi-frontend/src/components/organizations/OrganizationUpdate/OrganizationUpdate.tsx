import { OrganizationResponse } from '../../../dto/organization';

interface OrganizationUpdateProps {
  submitCallback: (response: OrganizationResponse) => void;
  organizationId: string;
  defaults: OrganizationResponse;
}

export const OrganizationUpdate = (
  props: OrganizationUpdateProps,
): JSX.Element => {
  return (<></>);
};
