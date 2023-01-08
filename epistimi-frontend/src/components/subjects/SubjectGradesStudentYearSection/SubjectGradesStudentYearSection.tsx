import './SubjectGradesStudentYearSection.scss';
import { IconChartLine, IconStar, IconStarHalf, IconUsers } from '@tabler/icons';
import { StudentGradesSubjectResponse } from '../../../dto/student-grades';
import { SubjectGradesStudentStatsTile } from '../SubjectGradesStudentStatsTile';

interface SubjectGradesStudentYearSectionProps {
  subjectGrades: StudentGradesSubjectResponse;
}

export const SubjectGradesStudentYearSection = (
  { subjectGrades }: SubjectGradesStudentYearSectionProps,
): JSX.Element => {
  return (
    <div className={'subject-grades-student-section'}>
      <div className={'subject-grades-student-section-summary'}>
        <SubjectGradesStudentStatsTile
          title={'Twoja średnia'}
          value={subjectGrades.average.student}
          icon={<IconChartLine size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Średnia klasy'}
          value={subjectGrades.average.subject}
          icon={<IconUsers size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Propozycja oceny rocznej'}
          value={subjectGrades.classification.proposal?.value.displayName}
          icon={<IconStarHalf size={18} color={'gray'}/>}
        />
        <SubjectGradesStudentStatsTile
          title={'Ocena roczna'}
          value={subjectGrades.classification.final?.value.displayName}
          icon={<IconStar size={18} color={'gray'}/>}
        />
      </div>
    </div>
  );
};

