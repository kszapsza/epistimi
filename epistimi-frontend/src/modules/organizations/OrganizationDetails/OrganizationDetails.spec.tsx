import { OrganizationDetails } from './OrganizationDetails';
import { render } from '../../../utils/test-render';
import { waitFor } from '@testing-library/react';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationDetails component', () => {
  it('should set page title', async () => {
    axiosMock.get.mockResolvedValue({});

    render(<OrganizationDetails/>);

    await waitFor(() => {
      expect(document.title).toBe('Szczegóły placówki – Epistimi');
    });
  });

  // it('', () => {});
});
