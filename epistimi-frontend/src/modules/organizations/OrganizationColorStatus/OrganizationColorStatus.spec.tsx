import { OrganizationColorStatus } from './OrganizationColorStatus';
import { OrganizationStatus } from '../../../dto/organization';
import { render } from '../../../utils/test-render';

describe('OrganizationColorStatus component', () => {
  it.each([
    [OrganizationStatus.ENABLED, 'AKTYWNA'],
    [OrganizationStatus.DISABLED, 'NIEAKTYWNA'],
  ])('should render a label (%s)', (
    status: OrganizationStatus,
    label: string,
  ) => {
    const { queryByText } = render(<OrganizationColorStatus status={status}/>);
    expect(queryByText(label)).not.toBeNull();
  });
});
