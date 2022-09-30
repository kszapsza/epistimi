import './OrganizationCreateStepper.scss';
import { Stepper } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface OrganizationCreateStepperProps {
  step: number;
}

export const OrganizationCreateStepper = (
  { step }: OrganizationCreateStepperProps
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <Stepper active={step} breakpoint={'sm'} className={'organization-create-stepper'}>
      <Stepper.Step
        label={t('organizations.organizationCreateStepper.organizationLabel')}
        description={t('organizations.organizationCreateStepper.organizationDesc')}/>
      <Stepper.Step
        label={t('organizations.organizationCreateStepper.adminLabel')}
        description={t('organizations.organizationCreateStepper.adminDesc')}/>
      <Stepper.Step
        label={t('organizations.organizationCreateStepper.summaryLabel')}
        description={t('organizations.organizationCreateStepper.summaryDesc')}/>
    </Stepper>
  );
};
