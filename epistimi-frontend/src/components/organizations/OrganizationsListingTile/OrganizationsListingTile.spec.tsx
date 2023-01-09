import { OrganizationsListingTile } from './OrganizationsListingTile';
import { render } from '../../../utils/test-render';

describe('OrganizationsListingTile component', () => {
  it('should render component', () => {
    const props = {
      id: '123',
      name: 'I LO',
      admin: 'Jan Muchomorek',
    };

    const { queryByText } = render(<OrganizationsListingTile {...props}/>);

    expect(queryByText('123')).toBeInTheDocument();
    expect(queryByText('I LO')).toBeInTheDocument();
    expect(queryByText(/Jan Muchomorek/i)).toBeInTheDocument();
  });
});
