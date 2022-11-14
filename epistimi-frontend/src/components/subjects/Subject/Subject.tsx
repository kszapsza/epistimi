import './Subject.scss';
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
    reload,
  } = useFetch<SubjectResponse>(`/api/subject/${subjectId}`);

  useDocumentTitle(subject && `${subject.name} – ${subject.course.code} (${subject.course.schoolYear})`);

  return (
    <>
      {loading && <LoaderBox/>}
      {subject && (
        <>
          <SubjectHead subject={subject}/>
          <SubjectNavigation subject={subject}/>
        </>
      )}
    </>
  );
};

/*
 * TODO: Tu trzeba wziąć pod uwagę, że do tego widoku ma mieć wgląd każdy
 *  (no, za wyjątkiem EPISTIMI_ADMIN):
 *
 *  - ORGANIZATION_ADMIN/TEACHER:
 *     - tab GRADES: jeśli prowadzi przedmiot, wystaw i przejrzyj oceny, jeśli nie, to dostęp do przedmiotu zabroniony,
 *     - tab FEED: dodawaj aktualności, reaguj, komentuj,
 *     - tab HOMEWORK: dodawaj i sprawdzaj zadania domowe,
 *
 *  - STUDENT/PARENT:
 *     - tab GRADES: przejrzyj oceny swoje lub swojego kida,
 *     - tab FEED: przeglądaj aktualności od nauczyciela, reaguj, komentuj,
 *     - tab HOMEWORK: przejrzyj zadania domowe swojego dziecka (rodzic) lub rozwiązuj (kid).
 */
