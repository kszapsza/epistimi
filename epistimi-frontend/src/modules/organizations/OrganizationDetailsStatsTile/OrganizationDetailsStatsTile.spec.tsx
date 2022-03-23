import { OrganizationDetailsStatsTile } from './OrganizationDetailsStatsTile';
import { render } from '../../../utils/test-render';

describe('OrganizationDetailsStatsTile component', () => {
  it('should render valid label/value pair', () => {
    const { getByText } = render(
      <OrganizationDetailsStatsTile
        label={'foo'}
        value={'bar'}
      />
    );

    expect(getByText('foo')).toBeInTheDocument();
    expect(getByText('bar')).toBeInTheDocument();
  });
});
