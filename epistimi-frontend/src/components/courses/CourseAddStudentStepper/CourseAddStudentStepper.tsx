import './CourseAddStudentStepper.scss';
import { Stepper } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface CourseAddStudentStepperProps {
  step: number;
}

export const CourseAddStudentStepper = (
  { step }: CourseAddStudentStepperProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <Stepper active={step} breakpoint={'sm'} className={'add-student-stepper'}>
      <Stepper.Step
        label={t('courses.courseAddStudentStepper.studentLabel')}
        description={t('courses.courseAddStudentStepper.studentDesc')}/>
      <Stepper.Step
        label={t('courses.courseAddStudentStepper.parentsLabel')}
        description={t('courses.courseAddStudentStepper.parentsDesc')}/>
      <Stepper.Step
        label={t('courses.courseAddStudentStepper.summaryLabel')}
        description={t('courses.courseAddStudentStepper.summaryDesc')}/>
    </Stepper>
  );
};
