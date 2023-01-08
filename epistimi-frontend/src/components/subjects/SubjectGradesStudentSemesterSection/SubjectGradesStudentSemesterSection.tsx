import './SubjectGradesStudentSemesterSection.scss';
import { IconChartLine, IconStar, IconStarHalf, IconUsers } from '@tabler/icons';
import { StudentGradesSubjectSemesterResponse } from '../../../dto/student-grades';
import { SubjectGradesStudentGradeTile } from '../SubjectGradesStudentGradeTile';
import { SubjectGradesStudentStatsTile } from '../SubjectGradesStudentStatsTile';
import { Title } from '@mantine/core';

interface SubjectGradesStudentSemesterSectionProps {
  title: string;
  semesterData: StudentGradesSubjectSemesterResponse;
}

export const SubjectGradesStudentSemesterSection = (
  { semesterData, title }: SubjectGradesStudentSemesterSectionProps,
): JSX.Element => {
  return (
    <div className={'subject-grades-student-section'}>
      <Title order={3}>
        {title}
      </Title>

      <div className={'subject-grades-student-section-summary'}>
        <SubjectGradesStudentStatsTile
          title={'Twoja średnia'}
          value={semesterData.average.student}
          icon={<IconChartLine size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Średnia klasy'}
          value={semesterData.average.subject}
          icon={<IconUsers size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Propozycja oceny'}
          value={semesterData.classification.proposal?.value.displayName}
          icon={<IconStarHalf size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Ocena'}
          value={semesterData.classification.final?.value.displayName}
          icon={<IconStar size={18} color={'gray'}/>}
        />
      </div>

      <div className={'subject-grades-student-section-grades'}>
        {semesterData.grades.map((grade) => (
          <SubjectGradesStudentGradeTile grade={grade}/>
        ))}
      </div>
    </div>
  );
};

