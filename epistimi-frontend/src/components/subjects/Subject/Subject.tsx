import './Subject.scss';
import { Alert } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { SubjectHead } from '../SubjectHead';
import { SubjectNavigation } from '../SubjectNavigation';
import { SubjectResponse } from '../../../dto/subject';
import { useDocumentTitle, useFetch } from '../../../hooks';
import { useParams } from 'react-router-dom';

export const Subject = (): JSX.Element => {
  const { subjectId } = useParams();

  const {
    data: subject,
    loading,
    error,
  } = useFetch<SubjectResponse>(`/api/subject/${subjectId}`);

  useDocumentTitle(subject && `${subject.name} – ${subject.course.code} (${subject.course.schoolYear})`);

  return (
    <>
      {loading && <LoaderBox/>}
      {error && (
        <Alert icon={<IconAlertCircle size={16}/>} title={'Wystąpił błąd'} color={'red'}>
          Nie udało się załadować widoku przedmiotu
        </Alert>
      )}
      {subject && (
        <>
          <SubjectHead subject={subject}/>
          <SubjectNavigation subject={subject}/>
        </>
      )}
    </>
  );
};
