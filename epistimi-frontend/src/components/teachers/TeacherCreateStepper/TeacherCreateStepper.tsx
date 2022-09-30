import './TeacherCreateStepper.scss';
import { Stepper } from '@mantine/core';
import { useTranslation } from 'react-i18next';

interface TeacherCreateStepperProps {
  step: number;
}

export const TeacherCreateStepper = (
  { step }: TeacherCreateStepperProps
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <Stepper active={step} breakpoint={'sm'} className={'teacher-create-stepper'}>
      <Stepper.Step
        label={t('teachers.teacherCreateStepper.teacherLabel')}
        description={t('teachers.teacherCreateStepper.teacherDesc')}/>
      <Stepper.Step
        label={t('teachers.teacherCreateStepper.summaryLabel')}
        description={t('teachers.teacherCreateStepper.summaryDesc')}/>
    </Stepper>
  );
};
