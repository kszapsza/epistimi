import './StudentsGrades.scss';
import { Alert, Select, Title } from '@mantine/core';
import { IconAlertCircle } from '@tabler/icons';
import { LoaderBox } from '../../common';
import { StudentsGradesResponse } from '../../../dto/student-grades';
import { StudentsGradesTable } from '../StudentsGradesTable';
import { useAppSelector } from '../../../store/hooks';
import { useEffect, useState } from 'react';
import { useFetch } from '../../../hooks';
import { UserRole } from '../../../dto/user';

export const StudentsGrades = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);
  const { data, loading, error } = useFetch<StudentsGradesResponse>(`/api/student/grade`);

  const [currentStudentId, setCurrentStudentId] = useState<string>();

  useEffect(() => {
    data?.students[0] && setCurrentStudentId(data.students[0].id);
  }, [data]);

  return (
    <>
      <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', marginBottom: '.5rem' }}>
        <Title order={2}>
          {user && user.role === UserRole.PARENT
            ? 'Oceny'
            : 'Twoje oceny'}
        </Title>
        {user && user.role === UserRole.PARENT && (
          <Select
            label={'Wybierz ucznia'}
            data={data?.students.map((student) => ({
              value: student.id,
              label: `${student.lastName} ${student.firstName}`,
            })) ?? []}
            onChange={(value) => value && setCurrentStudentId(value)}
            value={currentStudentId}
          />
        )}
      </div>

      {loading && <LoaderBox/>}
      {error && (
        <Alert icon={<IconAlertCircle size={16}/>} title={'Wystąpił błąd'} color={'red'}>
          Nie udało się załadować ocen ucznia
        </Alert>
      )}

      {data && (
        <StudentsGradesTable student={data.students.find((student) => student.id === currentStudentId)}/>
      )}
    </>
  );
};

