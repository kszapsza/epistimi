import { disabledOrganization, enabledOrganization } from '../../../stubs/organization';
import { fireEvent } from '@testing-library/react';
import { OrganizationStatus } from '../../../dto/organization';
import { OrganizationStatusChange } from './OrganizationStatusChange';
import { render } from '../../../utils/test-render';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationStatusChange component', () => {
  it('should render component (disable variant)', () => {
    const { getByRole, getByText } = render(
      <OrganizationStatusChange
        organization={enabledOrganization}
        onStatusChange={jest.fn()}
      />,
    );

    expect(getByText(/czy na pewno/i)).toContainHTML('Czy na pewno chcesz zdezaktywować placówkę <strong>SP7</strong>?');
    expect(getByRole('button')).toHaveTextContent(/dezaktywuj/i);
  });

  it('should send status change request (disable variant)', () => {
    axiosMock.put.mockResolvedValue({});

    const { getByRole } = render(
      <OrganizationStatusChange
        organization={enabledOrganization}
        onStatusChange={jest.fn()}
      />,
    );

    fireEvent.click(getByRole('button'));

    expect(axiosMock.put).toBeCalledWith(
      'api/organization/43/status',
      { status: OrganizationStatus.DISABLED },
    );
  });

  it('should render component (re-enable variant)', () => {
    const { getByRole, getByText } = render(
      <OrganizationStatusChange
        organization={disabledOrganization}
        onStatusChange={jest.fn()}
      />,
    );

    expect(getByText(/czy na pewno/i)).toContainHTML('Czy na pewno chcesz ponownie aktywować placówkę <strong>SP7</strong>?');
    expect(getByRole('button')).toHaveTextContent(/aktywuj/i);
  });

  it('should send status change request (re-enable variant)', () => {
    axiosMock.put.mockResolvedValue({});

    const { getByRole } = render(
      <OrganizationStatusChange
        organization={disabledOrganization}
        onStatusChange={jest.fn()}
      />,
    );

    fireEvent.click(getByRole('button'));

    expect(axiosMock.put).toBeCalledWith(
      'api/organization/44/status',
      { status: OrganizationStatus.ENABLED },
    );
  });
});
