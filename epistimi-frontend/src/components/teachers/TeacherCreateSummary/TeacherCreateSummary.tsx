import './TeacherCreateSummary.scss';
import { IconCheck, IconLock, IconUser } from '@tabler/icons';
import { Loader, TextInput, Title } from '@mantine/core';
import { TeacherRegisterResponse } from '../../../dto/teacher';

interface TeacherCreateSummaryProps {
  registerResponse?: TeacherRegisterResponse;
}

export const TeacherCreateSummary = (
  { registerResponse }: TeacherCreateSummaryProps,
): JSX.Element => {
  return (
    <div className={'teacher-create-summary'}>
      <div className={'teacher-create-summary-head'}>
        {!registerResponse && <Loader/>}
        {registerResponse && (
          <>
            <IconCheck size={60}/>
            <Title order={2}>
              Zarejestrowano nauczyciela
            </Title>
            {registerResponse.newUser.user.firstName} {registerResponse.newUser.user.lastName}
          </>
        )}
      </div>
      {registerResponse &&
        <div className={'teacher-create-summary-credentials'}>
          <TextInput
            icon={<IconUser size={18}/>}
            label={'Nazwa użytkownika'}
            readOnly={true}
            value={registerResponse.newUser.user.username}/>
          <TextInput
            icon={<IconLock size={18}/>}
            label={'Hasło'}
            readOnly={true}
            value={registerResponse.newUser.password}/>
        </div>}
    </div>
  );
};
