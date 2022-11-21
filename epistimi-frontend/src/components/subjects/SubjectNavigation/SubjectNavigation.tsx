import { IconHandTwoFingers, IconSettings, IconSpeakerphone, IconStar, IconWriting } from '@tabler/icons';
import { NotImplementedYet } from '../../common';
import { SubjectConfiguration } from '../SubjectConfiguration';
import { SubjectGradesStudent } from '../SubjectGradesStudent';
import { SubjectGradesTeacher } from '../SubjectGradesTeacher';
import { SubjectResponse } from '../../../dto/subject';
import { Tabs } from '@mantine/core';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

interface SubjectNavigationProps {
  subject: SubjectResponse;
}

export const SubjectNavigation = (
  { subject }: SubjectNavigationProps,
): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  const isUserSubjectTeacherOrAdmin = (): boolean => {
    const isUserSubjectTeacher = (): boolean => {
      return !!user
        && user.role == UserRole.TEACHER
        && subject.teacher.user.id === user.id;
    };

    const isUserOrganizationAdmin = (): boolean => {
      return !!user
        && user.role == UserRole.ORGANIZATION_ADMIN;
    };

    return isUserSubjectTeacher() || isUserOrganizationAdmin();
  };

  return (
    <>
      {user && (
        <Tabs defaultValue={'grades'}>
          <Tabs.List>
            <Tabs.Tab value={'grades'} icon={<IconStar size={14}/>}>
              Oceny
            </Tabs.Tab>
            {isUserSubjectTeacherOrAdmin() &&
              <Tabs.Tab value={'absence'} icon={<IconHandTwoFingers size={14}/>}>
                Realizacja zajęć
              </Tabs.Tab>}
            <Tabs.Tab value={'feed'} icon={<IconSpeakerphone size={14}/>}>
              Aktualności
            </Tabs.Tab>
            <Tabs.Tab value={'homework'} icon={<IconWriting size={14}/>}>
              Zadania domowe
            </Tabs.Tab>
            {isUserSubjectTeacherOrAdmin() &&
              <Tabs.Tab value={'configuration'} icon={<IconSettings size={14}/>} ml={'auto'}>
                Konfiguracja
              </Tabs.Tab>}
          </Tabs.List>

          <Tabs.Panel value={'grades'} p={'md'}>
            {isUserSubjectTeacherOrAdmin()
              ? <SubjectGradesTeacher subject={subject}/>
              : <SubjectGradesStudent/>}
          </Tabs.Panel>
          <Tabs.Panel value={'absence'} p={'md'}>
            <NotImplementedYet/>
          </Tabs.Panel>
          <Tabs.Panel value={'feed'} p={'md'}>
            <NotImplementedYet/>
          </Tabs.Panel>
          <Tabs.Panel value={'homework'} p={'md'}>
            <NotImplementedYet/>
          </Tabs.Panel>
          <Tabs.Panel value={'configuration'} p={'md'}>
            <SubjectConfiguration subject={subject}/>
          </Tabs.Panel>
        </Tabs>
      )}
    </>
  );
};