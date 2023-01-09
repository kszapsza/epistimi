import { IconSettings, IconStar } from '@tabler/icons';
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
              <Tabs.Tab value={'configuration'} icon={<IconSettings size={14}/>}>
                Konfiguracja
              </Tabs.Tab>}
          </Tabs.List>

          <Tabs.Panel value={'grades'} p={'md'}>
            {isUserSubjectTeacherOrAdmin()
              ? <SubjectGradesTeacher subject={subject}/>
              : <SubjectGradesStudent/>}
          </Tabs.Panel>
          {isUserSubjectTeacherOrAdmin() &&
            <Tabs.Panel value={'configuration'} p={'md'}>
              <SubjectConfiguration subject={subject}/>
            </Tabs.Panel>}
        </Tabs>
      )}
    </>
  );
};