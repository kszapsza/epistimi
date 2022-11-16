import './SubjectGradesTeacher.scss';
import { Alert, Modal, Title } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { SubjectGradesResponse } from '../../../dto/subject-grades';
import { SubjectGradesTeacherIssueForm } from '../SubjectGradesTeacherIssueForm';
import { SubjectGradesTeacherTable } from '../SubjectGradesTeacherTable';
import { SubjectResponse } from '../../../dto/subject';
import { useFetch } from '../../../hooks';
import { useState } from 'react';

interface SubjectGradesTeacherAdminProps {
  subject: SubjectResponse;
}

interface SubjectGradesIssueFormState {
  studentId: string;
  semester: number;
}

export const SubjectGradesTeacher = (
  { subject }: SubjectGradesTeacherAdminProps,
): JSX.Element => {

  const [issueFormContext, setIssueFormContext] = useState<SubjectGradesIssueFormState | null>(null);

  const {
    data,
    loading,
    error,
    reload,
  } = useFetch<SubjectGradesResponse>(`/api/subject/${subject.id}/grade`);

  return (
    <div className={'subject-grades-teacher'}>
      {loading && <LoaderBox/>}
      {error && (
        <Alert icon={<IconAlertCircle size={16}/>} title={'Wystąpił błąd'} color={'red'}>
          Nie udało się załadować ocen z&nbsp;przedmiotu
        </Alert>
      )}

      {data && issueFormContext && (
        <Modal
          onClose={() => setIssueFormContext(null)}
          opened={!!issueFormContext}
          size={'lg'}
          title={'Wystaw ocenę'}
        >
          <SubjectGradesTeacherIssueForm
            subject={subject}
            student={data.students
              .find((student) => student.id === issueFormContext.studentId)!
            }
            semester={issueFormContext.semester}/>
        </Modal>
      )}

      {data && (
        <>
          <Title order={3}>
            Wystawianie ocen
          </Title>
          <SubjectGradesTeacherTable
            subjectGradesResponse={data}
            onIssueGradeClick={(studentId, semester) => setIssueFormContext({ studentId, semester })}
          />
        </>
      )}
    </div>
  );
};