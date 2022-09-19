import './CourseAddStudentSummary.scss';
import { IconCheck, IconLock, IconUser } from '@tabler/icons';
import { Loader, TextInput, Title } from '@mantine/core';
import { StudentRegisterResponse } from '../../../dto/student';
import { useTranslation } from 'react-i18next';

interface CourseAddStudentSummaryProps {
  registerResponse?: StudentRegisterResponse;
}

export const CourseAddStudentSummary = (
  { registerResponse }: CourseAddStudentSummaryProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <div className={'add-student-summary'}>
      <div className={'add-student-summary-head'}>
        {!registerResponse && <Loader/>}
        {registerResponse && (
          <>
            <IconCheck size={60}/>
            <Title order={2}>
              {t('courses.courseAddStudentSummary.studentRegistered')}
            </Title>
            {registerResponse.student.user.firstName} {registerResponse.student.user.lastName}
          </>
        )}
      </div>
      {registerResponse && (
        <>
          <div className={'add-student-summary-credentials'}>
            <TextInput
              icon={<IconUser size={18}/>}
              label={t('courses.courseAddStudentSummary.username')}
              readOnly={true}
              value={registerResponse.student.user.username}/>
            <TextInput
              icon={<IconLock size={18}/>}
              label={t('courses.courseAddStudentSummary.password')}
              readOnly={true}
              value={registerResponse.student.password}/>
          </div>

          <Title order={3}>
            {t('courses.courseAddStudentSummary.parentsRegistered')}
          </Title>

          {registerResponse.parents.map((parentResponse, idx) => (
            <div className={'add-student-summary-credentials'} key={idx}>
              <Title order={4}>
                {parentResponse.parent.user.firstName} {parentResponse.parent.user.lastName}
              </Title>
              <TextInput
                icon={<IconUser size={18}/>}
                label={t('courses.courseAddStudentSummary.username')}
                readOnly={true}
                value={parentResponse.parent.user.username}/>
              <TextInput
                icon={<IconLock size={18}/>}
                label={t('courses.courseAddStudentSummary.password')}
                readOnly={true}
                value={parentResponse.password}/>
            </div>
          ))}
        </>
      )}
    </div>
  );
};
