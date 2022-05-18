import './CourseAddStudentStepper.scss';
import { Stepper } from '@mantine/core';

interface CourseAddStudentStepperProps {
  step: number;
}

export const CourseAddStudentStepper = (
  { step }: CourseAddStudentStepperProps,
): JSX.Element => {
  return (
    <Stepper active={step} breakpoint={'sm'} className={'add-student-stepper'}>
      <Stepper.Step label={'Uczeń'} description={'Edytuj dane ucznia'}/>
      <Stepper.Step label={'Rodzice'} description={'Edytuj dane rodziców'}/>
      <Stepper.Step label={'Podsumowanie'} description={'Wygenerowane loginy i hasła'}/>
    </Stepper>
  );
};
