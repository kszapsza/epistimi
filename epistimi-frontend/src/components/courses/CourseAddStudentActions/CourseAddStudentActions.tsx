import './CourseAddStudentActions.scss';
import { Button } from '@mantine/core';
import { CourseAddStudentStep, UserFormData } from '../CourseAddStudent';
import { IconArrowLeft, IconArrowRight, IconCheck, IconPlus } from '@tabler/icons';
import { useTranslation } from 'react-i18next';

interface CourseAddStudentActionsProps {
  modalStep: CourseAddStudentStep;
  parentsList: readonly UserFormData[];
  onEditParentsClick: () => void;
  onEditStudentClick: () => void;
  onAddParentClick: () => void;
  onSubmitClick: () => void;
  sendingRequest: boolean;
}

export const CourseAddStudentActions = (
  {
    modalStep,
    onAddParentClick,
    onEditParentsClick,
    onEditStudentClick,
    onSubmitClick,
    parentsList,
    sendingRequest,
  }: CourseAddStudentActionsProps
): JSX.Element => {

  const { t } = useTranslation();

  return (
    <div className={'add-student-actions'}>
      {modalStep === CourseAddStudentStep.EDIT_STUDENT && (
        <Button
          rightIcon={<IconArrowRight size={18}/>}
          variant={'outline'}
          onClick={onEditParentsClick}
        >
          {t('courses.courseAddStudentActions.parentsData')}
        </Button>)}
      {modalStep === CourseAddStudentStep.EDIT_PARENTS && (
        <>
          <Button
            leftIcon={<IconArrowLeft size={18}/>}
            variant={'outline'}
            onClick={onEditStudentClick}
          >
            {t('courses.courseAddStudentActions.studentData')}
          </Button>
          {parentsList.length < 2 && (
            <Button
              leftIcon={<IconPlus size={18}/>}
              variant={'outline'}
              onClick={onAddParentClick}
            >
              {t('courses.courseAddStudentActions.addParent')}
            </Button>)}
          {parentsList.length >= 1 && (
            <Button
              leftIcon={<IconCheck size={18}/>}
              onClick={onSubmitClick}
              loading={sendingRequest}
            >
              {t('courses.courseAddStudentActions.addStudent')}
            </Button>)}
        </>)}
    </div>
  );
};