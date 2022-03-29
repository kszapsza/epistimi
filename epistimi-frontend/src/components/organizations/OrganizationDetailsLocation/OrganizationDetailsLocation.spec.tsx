import { OrganizationDetailsLocation } from './OrganizationDetailsLocation';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetailsLocation component', () => {
  it('should render address information', async () => {
    axiosMock.get.mockResolvedValue({});

    const { getByText } = render(
      <OrganizationDetailsLocation
        address={{
          street: 'Szkolna 17',
          postalCode: '15-640',
          city: 'Białystok',
          countryCode: 'PL',
        }}
      />);

    await waitFor(() => {
      expect(getByText(/ulica/i).parentElement).toHaveTextContent(/Szkolna 17/);
      expect(getByText(/kod pocztowy/i).parentElement).toHaveTextContent(/15-640/);
      expect(getByText(/miasto/i).parentElement).toHaveTextContent(/Białystok/);
      expect(getByText(/kraj/i).parentElement).toHaveTextContent(/Polska/);
    });
  });

  // TODO: check if map is hidden if there are no coordinates
});
