import { SubjectGradesResponse } from '../../../dto/subject-grades';
import { SubjectGradesTeacherGradesCell } from '../SubjectGradesTeacherGradesCell';
import { Table } from '@mantine/core';

interface SubjectGradesTeacherTableProps {
  subjectGradesResponse: SubjectGradesResponse;
  onIssueGradeClick: (studentId: string, semester: number) => void;
}

export const SubjectGradesTeacherTable = (
  { subjectGradesResponse, onIssueGradeClick }: SubjectGradesTeacherTableProps,
): JSX.Element => {
  return (
    <Table striped>
      <thead>
      <tr>
        <th rowSpan={2} style={{ width: '1%' }}>#</th>
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
      {subjectGradesResponse.students.map((student, idx) =>
        (
          <tr key={student.id}>
            <td>
              {idx + 1}
            </td>
            <td>
              {student.lastName} {student.firstName}
            </td>
            <td>
              {<SubjectGradesTeacherGradesCell
                grades={student.firstSemester.grades}
                onIssueGradeClick={() => onIssueGradeClick(student.id, 1)}
              />}
            </td>
            <td>
              {student.firstSemester.average || '—'}
            </td>
            <td>
              {<SubjectGradesTeacherGradesCell
                grades={student.secondSemester.grades}
                onIssueGradeClick={() => onIssueGradeClick(student.id, 2)}
              />}
            </td>
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
        <th>{subjectGradesResponse.average.firstSemester || '—'}</th>
        <th></th>
        <th>{subjectGradesResponse.average.secondSemester || '—'}</th>
        <th>{subjectGradesResponse.average.overall || '—'}</th>
      </tr>
      </tfoot>
    </Table>
  );
};