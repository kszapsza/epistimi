import './StudentsGradesTable.scss';
import { GradeBadgesTableCell } from '../GradeBadgesTableCell';
import { Link } from 'react-router-dom';
import { StudentGradesResponse } from '../../../dto/student-grades';
import { Table } from '@mantine/core';

interface StudentsGradesTableProps {
  student?: StudentGradesResponse;
}

export const StudentsGradesTable = (
  { student }: StudentsGradesTableProps,
): JSX.Element => {
  return (
    <Table striped>
      <thead>
      <tr>
        <th rowSpan={2} style={{ width: '1%' }}>#</th>
        <th rowSpan={2} style={{ width: '20%' }}>Przedmiot</th>
        <th colSpan={4}>Semestr&nbsp;I</th>
        <th colSpan={4}>Semestr&nbsp;II</th>
        <th colSpan={3}>Roczna</th>
      </tr>
      <tr>
        <th style={{ width: '30%' }}>Oceny cząstkowe</th>
        <th>Śr.&nbsp;I</th>
        <th>(I)</th>
        <th>I</th>
        <th style={{ width: '30%' }}>Oceny cząstkowe</th>
        <th>Śr.&nbsp;II</th>
        <th>(II)</th>
        <th>II</th>
        <th>Śr.&nbsp;R</th>
        <th>(R)</th>
        <th>R</th>
      </tr>
      </thead>
      <tbody>
      {student?.subjects.map((subject, idx) =>
          (
            <tr key={subject.id}>
              <td>
                {idx + 1}
              </td>
              <td>
                <Link to={`/app/subjects/${subject.id}`}>{subject.name}</Link>
              </td>
              <td>
                {<GradeBadgesTableCell grades={subject.firstSemester.grades}/>}
              </td>
              <td>
                {subject.firstSemester.average.student || '—'}
              </td>
              <td>—</td>
              <td>—</td>
              <td>
                {<GradeBadgesTableCell grades={subject.secondSemester.grades}/>}
              </td>
              <td>{subject.secondSemester.average.student || '—'}</td>
              <td>—</td>
              <td>—</td>
              <td>{subject.average.student || '—'}</td>
              <td>—</td>
              <td>—</td>
            </tr>
          ))}
      </tbody>
    </Table>
  );
};

