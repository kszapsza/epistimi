import './StudentGrades.scss';
import { GradeBadgesTableCell } from '../GradeBadgesTableCell';
import { StudentGradesResponse } from '../../../dto/student-grades';
import { Table, Title } from '@mantine/core';
import { useFetch } from '../../../hooks';

/*interface StudentGradesProps {
}*/

export const StudentGrades = (
  // {}: StudentGradesProps,
): JSX.Element => {

  const { data, loading, error, reload } = useFetch<StudentGradesResponse>(`/api/student/grade`);

  if (!data) {
    return <></>;
  }

  return (
    <>
      <Title order={2}>
        Twoje oceny
      </Title>
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
        {data.subjects.map((subject, idx) =>
          (
            <tr key={subject.id}>
              <td>
                {idx + 1}
              </td>
              <td>
                {subject.name}
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
        {/*<tfoot>
      <tr>
        <th></th>
        <th></th>
        <th></th>
        <th>{data.average.firstSemester || '—'}</th>
        <th></th>
        <th></th>
        <th></th>
        <th>{subjectGradesResponse.average.secondSemester || '—'}</th>
        <th></th>
        <th></th>
        <th>{subjectGradesResponse.average.overall || '—'}</th>
        <th></th>
        <th></th>
      </tr>
      </tfoot>*/}
      </Table>
    </>
  );
};

