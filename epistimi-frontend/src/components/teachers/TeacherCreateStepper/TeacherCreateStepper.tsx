import './TeacherCreateStepper.scss';
import { Stepper } from '@mantine/core';

interface TeacherCreateStepperProps {
  step: number;
}

export const TeacherCreateStepper = (
  { step }: TeacherCreateStepperProps
): JSX.Element => {
  return (
    <Stepper active={step} breakpoint={'sm'} className={'teacher-create-stepper'}>
      <Stepper.Step label={'Nauczyciel'} description={'Podaj dane nauczyciela'}/>
      <Stepper.Step label={'Podsumowanie'} description={'Wygenerowane loginy i hasła'}/>
    </Stepper>
  );
};
