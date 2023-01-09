import { ClassificationGradeBadge } from '../../grades/ClassificationGradeBadge';
import { GradeBadgesTableCell } from '../../grades';
import { SubjectGradesResponse } from '../../../dto/subject-grades';
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
              {<GradeBadgesTableCell
                grades={student.firstSemester.grades}
                onIssueGradeClick={() => onIssueGradeClick(student.id, 1)}
              />}
            </td>
            <td>
              {student.firstSemester.average || '—'}
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.firstSemester.classification.proposal}
                header={'Ocena – semestr I (prop.)'}
              />
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.firstSemester.classification.final}
                header={'Ocena – semestr I'}
              />
            </td>
            <td>
              {<GradeBadgesTableCell
                grades={student.secondSemester.grades}
                onIssueGradeClick={() => onIssueGradeClick(student.id, 2)}
              />}
            </td>
            <td>
              {student.secondSemester.average || '—'}
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.secondSemester.classification.proposal}
                header={'Ocena – semestr II (prop.)'}
              />
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.secondSemester.classification.final}
                header={'Ocena – semestr II'}
              />
            </td>
            <td>
              {student.average || '—'}
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.classification.proposal}
                header={'Ocena roczna (prop.)'}
              />
            </td>
            <td>
              <ClassificationGradeBadge
                grade={student.classification.final}
                header={'Ocena roczna'}
              />
            </td>
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
        <th></th>
        <th></th>
        <th>{subjectGradesResponse.average.secondSemester || '—'}</th>
        <th></th>
        <th></th>
        <th>{subjectGradesResponse.average.overall || '—'}</th>
        <th></th>
        <th></th>
      </tr>
      </tfoot>
    </Table>
  );
};