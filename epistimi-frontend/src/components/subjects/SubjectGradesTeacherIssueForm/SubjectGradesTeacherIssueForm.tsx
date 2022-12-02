import './SubjectGradesTeacherIssueForm.scss';
import { GradeResponse } from '../../../dto/grade';
import { IconStar, IconStars } from '@tabler/icons';
import { SubjectGradesStudentResponse } from '../../../dto/subject-grades';
import { SubjectGradesTeacherIssueClassificationGradeForm } from '../SubjectGradesTeacherIssueClassificationGradeForm';
import { SubjectGradesTeacherIssueGradeForm } from '../SubjectGradesTeacherIssueGradeForm';
import { SubjectResponse } from '../../../dto/subject';
import { Tabs } from '@mantine/core';

interface SubjectGradesTeacherIssueFormProps {
  subject: SubjectResponse;
  student: SubjectGradesStudentResponse;
  semester: number;
  onNewGradeIssued: (response: GradeResponse) => void;
}

export const SubjectGradesTeacherIssueForm = (
  props: SubjectGradesTeacherIssueFormProps,
): JSX.Element => {
  return (
    <>
      <Tabs defaultValue={'grade'}>
        <Tabs.List>
          <Tabs.Tab value={'grade'} icon={<IconStars size={14}/>}>
            Ocena cząstkowa
          </Tabs.Tab>
          <Tabs.Tab value={'classification-grade'} icon={<IconStar size={14}/>}>
            Ocena klasyfikacyjna
          </Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value={'grade'} pt={'sm'}>
          <SubjectGradesTeacherIssueGradeForm {...props} />
        </Tabs.Panel>
        <Tabs.Panel value={'classification-grade'} pt={'sm'}>
          <SubjectGradesTeacherIssueClassificationGradeForm {...props} />
        </Tabs.Panel>
      </Tabs>
    </>
  );
};

