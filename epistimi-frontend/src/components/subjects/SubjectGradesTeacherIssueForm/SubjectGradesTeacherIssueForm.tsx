import './SubjectGradesTeacherIssueForm.scss';
import { GradeIssueRequest, GradeValue } from '../../../dto/grade';
import { SubjectGradesStudentResponse } from '../../../dto/subject-grades';
import { SubjectResponse } from '../../../dto/subject';
import { useForm } from '@mantine/form';

interface SubjectGradesTeacherIssueFormProps {
  subject: SubjectResponse;
  student: SubjectGradesStudentResponse;
  semester: number;
}

export const SubjectGradesTeacherIssueForm = (
  { subject, student, semester }: SubjectGradesTeacherIssueFormProps,
): JSX.Element => {

  const form = useForm<GradeIssueRequest>({
    initialValues: {
      subjectId: subject.id,
      studentId: student.id,
      categoryId: '',
      value: GradeValue.ACCEPTABLE,
      semester: semester,
      weight: 1,
      countTowardsAverage: true,
    },
    validate: () => ({}),
  });

  return (
    <form noValidate>
      Przedmiot: {subject.name}<br/>
      Student: {student.lastName} {student.firstName}<br/>
      Semestr: {semester}
    </form>
  );
};

