import { GradesWithStatisticsResponse } from '../../../dto/grades';
import { LoaderBox } from '../../common';
import { SubjectResponse } from '../../../dto/subject';
import { Table, Title } from '@mantine/core';
import { useFetch } from '../../../hooks';

interface SubjectGradesTeacherAdminProps {
  subject: SubjectResponse;
}

export const SubjectGradesTeacherAdmin = (
  { subject }: SubjectGradesTeacherAdminProps,
): JSX.Element => {

  const {
    data: gradesReport,
    loading,
    error,
    reload,
  } = useFetch<GradesWithStatisticsResponse>(`/api/subject/${subject.id}/grade`);

  if (!gradesReport || loading) {
    return <LoaderBox/>;
  }

  const rows = gradesReport.subjects[0].students.map((student) => {
    return (
      <tr key={student.id}>
        <td>{student.lastName}</td>
        <td>{student.firstName}</td>
        <td>{student.grades.filter((grade) => grade.semester === 1).map((grade) => grade.value.displayName).join(', ') || '–'}</td>
        <td>{student.statistics.average.firstSemester || '–'}</td>
        <td>{student.grades.filter((grade) => grade.semester === 2).map((grade) => grade.value.displayName).join(', ') || '–'}</td>
        <td>{student.statistics.average.secondSemester || '–'}</td>
        <td>{student.statistics.average.overall || '–'}</td>
      </tr>
    );
  });

  return (
    <>
      <Title order={3}>
        Wystawianie ocen
      </Title>
      <Table>
        <thead>
        <tr>
          <th>Nazwisko</th>
          <th>Imię</th>
          <th>Oceny (I sem.)</th>
          <th>Średnia (I sem.)</th>
          <th>Oceny (II sem.)</th>
          <th>Średnia (II sem.)</th>
          <th>Średnia (roczna)</th>
        </tr>
        </thead>
        <tbody>{rows}</tbody>
      </Table>
    </>
  );
};