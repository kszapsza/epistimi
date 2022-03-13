import { OrganizationDetailsKeyValue } from './OrganizationDetailsKeyValue';
import { render } from '../../../utils/test-render';

describe('OrganizationDetailsKeyValue component', () => {
  it('should render valid label/value pair', () => {
    const { getByText } = render(
      <OrganizationDetailsKeyValue
        label={'foo'}
        value={'bar'}
      />,
    );

    expect(getByText('foo')).toBeInTheDocument();
    expect(getByText('bar')).toBeInTheDocument();
  });
});
