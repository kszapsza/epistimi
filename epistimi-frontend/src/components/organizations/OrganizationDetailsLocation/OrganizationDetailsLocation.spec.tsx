import { OrganizationDetailsLocation } from './OrganizationDetailsLocation';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetailsLocation component', () => {

  const STREET_LABEL_REGEXP = /organizations\.organizationDetailsLocation\.street/;
  const POSTAL_CODE_LABEL_REGEXP = /organizations\.organizationDetailsLocation\.postalCode/;
  const CITY_LABEL_REGEXP = /organizations\.organizationDetailsLocation\.city/;

  it('should render address information', async () => {
    axiosMock.get.mockResolvedValue({});

    const { getByText } = render(
      <OrganizationDetailsLocation
        address={{
          street: 'Słonimska 1',
          postalCode: '15-950',
          city: 'Białystok',
        }}
      />);

    await waitFor(() => {
      expect(getByText(STREET_LABEL_REGEXP).parentElement).toHaveTextContent(/Słonimska 1/);
      expect(getByText(POSTAL_CODE_LABEL_REGEXP).parentElement).toHaveTextContent(/15-950/);
      expect(getByText(CITY_LABEL_REGEXP).parentElement).toHaveTextContent(/Białystok/);
    });
  });
});
