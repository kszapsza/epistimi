import './SubjectGradesStudent.scss';
import { Alert, LoadingOverlay } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { StudentsGradesResponse } from '../../../dto/student-grades';
import { SubjectGradesStudentSemesterSection } from '../SubjectGradesStudentSemesterSection';
import { SubjectGradesStudentYearSection } from '../SubjectGradesStudentYearSection';
import { useFetch } from '../../../hooks';
import { useParams } from 'react-router-dom';

export const SubjectGradesStudent = (): JSX.Element => {
  const { subjectId } = useParams();
  const { data, loading, error } = useFetch<StudentsGradesResponse>(`/api/student/grade?subjectId=${subjectId}`);

  return (
    <div className={'subject-grades-student'}>
      <LoadingOverlay visible={loading}/>
      {error && (
        <Alert icon={<IconAlertCircle size={16}/>} title={'Wystąpił błąd'} color={'red'}>
          Nie udało się załadować widoku ocen
        </Alert>
      )}
      {data && (
        <>
          <SubjectGradesStudentYearSection
            subjectGrades={data.students[0].subjects[0]}
          />
          <SubjectGradesStudentSemesterSection
            title={'Semestr I'}
            semesterData={data.students[0].subjects[0].firstSemester}
          />
          <SubjectGradesStudentSemesterSection
            title={'Semestr II'}
            semesterData={data.students[0].subjects[0].secondSemester}
          />
        </>
      )}
    </div>
  );
};