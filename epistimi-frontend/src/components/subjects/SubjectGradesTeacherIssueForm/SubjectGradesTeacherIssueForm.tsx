import './SubjectGradesTeacherIssueForm.scss';
import { ClassificationGradeResponse } from '../../../dto/classification-grade';
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
  onNewClassificationGradeIssued: (response: ClassificationGradeResponse) => void;
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
          <SubjectGradesTeacherIssueGradeForm
            onNewGradeIssued={props.onNewGradeIssued}
            subject={props.subject}
            student={props.student}
            semester={props.semester}
          />
        </Tabs.Panel>
        <Tabs.Panel value={'classification-grade'} pt={'sm'}>
          <SubjectGradesTeacherIssueClassificationGradeForm
            onNewClassificationGradeIssued={props.onNewClassificationGradeIssued}
            subject={props.subject}
            student={props.student}
            semester={props.semester}
          />
        </Tabs.Panel>
      </Tabs>
    </>
  );
};

