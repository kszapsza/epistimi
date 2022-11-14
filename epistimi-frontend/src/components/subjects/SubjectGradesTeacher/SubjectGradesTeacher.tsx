import { LoaderBox } from '../../common';
import { SubjectGradesResponse, SubjectGradesStudentResponse } from '../../../dto/subject-grades';
import { SubjectGradesTeacherGradeCell } from '../SubjectGradesTeacherGradeCell';
import { Table } from '@mantine/core';
import { useFetch } from '../../../hooks';

interface SubjectGradesTeacherAdminProps {
  subjectId: string;
}

export const SubjectGradesTeacher = (
  { subjectId }: SubjectGradesTeacherAdminProps,
): JSX.Element => {

  const {
    data: subject,
    loading,
    error,
    reload,
  } = useFetch<SubjectGradesResponse>(`/api/subject/${subjectId}/grade`);

  if (!subject || loading) {
    return <LoaderBox/>;
  }

  return (
    <Table striped>
      <thead>
      <tr>
        <th rowSpan={2} style={{ width: '1%' }}></th>
        <th rowSpan={2} style={{ width: '20%' }}>Nazwisko i&nbsp;imię</th>
        <th colSpan={2}>Semestr&nbsp;I</th>
        <th colSpan={2}>Semestr&nbsp;II</th>
        <th>Roczna</th>
      </tr>
      <tr>
        <th style={{ width: '30%' }}>Oceny</th>
        <th>Śr.</th>
        <th style={{ width: '30%' }}>Oceny</th>
        <th>Śr.</th>
        <th>Śr.</th>
      </tr>
      </thead>
      <tbody>
      {subject.students.map((student: SubjectGradesStudentResponse, idx: number) =>
        (
          <tr key={student.id}>
            <td>{idx + 1}</td>
            <td>{student.lastName} {student.firstName}</td>
            <td>{<SubjectGradesTeacherGradeCell grades={student.firstSemester.grades}/>}</td>
            <td>{student.firstSemester.average || '—'}</td>
            <td>{<SubjectGradesTeacherGradeCell grades={student.secondSemester.grades}/>}</td>
            <td>{student.secondSemester.average || '—'}</td>
            <td>{student.average || '—'}</td>
          </tr>
        ))}
      </tbody>
      <tfoot>
      <tr>
        <th></th>
        <th></th>
        <th></th>
        <th>{subject.average.firstSemester || '—'}</th>
        <th></th>
        <th>{subject.average.secondSemester || '—'}</th>
        <th>{subject.average.overall || '—'}</th>
      </tr>
      </tfoot>
    </Table>
  );
};