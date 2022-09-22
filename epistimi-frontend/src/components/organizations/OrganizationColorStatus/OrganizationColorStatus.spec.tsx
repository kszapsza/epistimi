import { OrganizationColorStatus } from './OrganizationColorStatus';
import { OrganizationStatus } from '../../../dto/organization';
import { render } from '../../../utils/test-render';

describe('OrganizationColorStatus component', () => {

  const ENABLED_LABEL_REGEXP = /organizations\.organizationColorStatus\.enabled/i;
  const DISABLED_LABEL_REGEXP = /organizations\.organizationColorStatus\.disabled/i;

  it.each([
    [OrganizationStatus.ENABLED, ENABLED_LABEL_REGEXP],
    [OrganizationStatus.DISABLED, DISABLED_LABEL_REGEXP],
  ])('should render a label (%s)', (
    status: OrganizationStatus,
    labelRegexp: RegExp,
  ) => {
    const { queryByText } = render(<OrganizationColorStatus status={status}/>);
    expect(queryByText(labelRegexp)).not.toBeNull();
  });
});
