import { OrganizationsListingTile } from './OrganizationsListingTile';
import { OrganizationStatus } from '../../../dto/organization';
import { render } from '../../../utils/test-render';

describe('OrganizationsListingTile component', () => {

  const ENABLED_LABEL_REGEXP = /organizations\.organizationColorStatus\.enabled/i;

  it('should render component', () => {
    const props = {
      id: '123',
      name: 'I LO',
      admin: 'Jan Muchomorek',
      status: OrganizationStatus.ENABLED,
    };

    const { queryByText } = render(<OrganizationsListingTile {...props}/>);

    expect(queryByText('123')).toBeInTheDocument();
    expect(queryByText('I LO')).toBeInTheDocument();
    expect(queryByText(/Jan Muchomorek/i)).toBeInTheDocument();
    expect(queryByText(ENABLED_LABEL_REGEXP)).toBeInTheDocument();
  });
});
