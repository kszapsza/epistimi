import './TeacherCreateSummary.scss';
import { IconCheck, IconLock, IconUser } from '@tabler/icons';
import { Loader, TextInput, Title } from '@mantine/core';
import { TeacherRegisterResponse } from '../../../dto/teacher';
import { useTranslation } from 'react-i18next';

interface TeacherCreateSummaryProps {
  registerResponse?: TeacherRegisterResponse;
}

export const TeacherCreateSummary = (
  { registerResponse }: TeacherCreateSummaryProps,
): JSX.Element => {
  const { t } = useTranslation();
  return (
    <div className={'teacher-create-summary'}>
      <div className={'teacher-create-summary-head'}>
        {!registerResponse && <Loader/>}
        {registerResponse && (
          <>
            <IconCheck size={60}/>
            <Title order={2}>
              {t('teachers.teacherCreateSummary.teacherRegistered')}
            </Title>
            {registerResponse.newUser.user.firstName} {registerResponse.newUser.user.lastName}
          </>
        )}
      </div>
      {registerResponse &&
        <div className={'teacher-create-summary-credentials'}>
          <TextInput
            icon={<IconUser size={18}/>}
            label={t('teachers.teacherCreateSummary.username')}
            readOnly={true}
            value={registerResponse.newUser.user.username}/>
          <TextInput
            icon={<IconLock size={18}/>}
            label={t('teachers.teacherCreateSummary.password')}
            readOnly={true}
            value={registerResponse.newUser.password}/>
        </div>}
    </div>
  );
};
