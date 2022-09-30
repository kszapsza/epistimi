import { disabledOrganization, enabledOrganization } from '../../../stubs/organization';
import { fireEvent } from '@testing-library/react';
import { OrganizationStatus } from '../../../dto/organization';
import { OrganizationStatusChange } from './OrganizationStatusChange';
import { render } from '../../../utils/test-render';
import axios from 'axios';

jest.mock('axios');
const axiosMock = axios as jest.Mocked<typeof axios>;

describe('OrganizationStatusChange component', () => {

  const DISABLE_CONFIRMATION_TEXT_REGEXP = /organizations\.organizationStatusChange\.areYouSureYouWantToDisable/;
  const DISABLE_CONFIRMATION_BUTTON_REGEXP = /organizations\.organizationStatusChange\.disable/;
  const ENABLE_CONFIRMATION_TEXT_REGEXP = /organizations\.organizationStatusChange\.areYouSureYouWantToEnable/;
  const ENABLE_CONFIRMATION_BUTTON_REGEXP = /organizations\.organizationStatusChange\.enable/;

  it('should render component (disable variant)', () => {
    const { getByRole, getByText } = render(
      <OrganizationStatusChange
        organization={enabledOrganization}
        onStatusChange={jest.fn()}
      />,
    );

    const confirmationText = getByText(DISABLE_CONFIRMATION_TEXT_REGEXP);
    const confirmationButton = getByRole('button');

    expect(confirmationText).toBeTruthy();
    expect(confirmationButton).toHaveTextContent(DISABLE_CONFIRMATION_BUTTON_REGEXP);
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

    const confirmationText = getByText(ENABLE_CONFIRMATION_TEXT_REGEXP);
    const confirmationButton = getByRole('button');

    expect(confirmationText).toBeTruthy();
    expect(confirmationButton).toHaveTextContent(ENABLE_CONFIRMATION_BUTTON_REGEXP);
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
