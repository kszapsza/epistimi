import './OrganizationCreateStepper.scss';
import { Stepper } from '@mantine/core';

interface OrganizationCreateStepperProps {
  step: number;
}

export const OrganizationCreateStepper = (
  { step }: OrganizationCreateStepperProps
): JSX.Element => {
  return (
    <Stepper active={step} breakpoint={'sm'} className={'organization-create-stepper'}>
      <Stepper.Step label={'Placówka'} description={'Dane placówki'}/>
      <Stepper.Step label={'Administrator'} description={'Dane administratora'}/>
      <Stepper.Step label={'Podsumowanie'} description={'Login i hasło administratora'}/>
    </Stepper>
  );
};
