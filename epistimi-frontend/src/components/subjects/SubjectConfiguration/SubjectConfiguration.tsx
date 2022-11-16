import './SubjectConfiguration.scss';
import { ActionIcon, Button, ColorSwatch, Modal, Table, Title } from '@mantine/core';
import { GradeCategoriesResponse, GradeCategoriesResponseEntry } from '../../../dto/grade-category';
import { IconPencil, IconPlus } from '@tabler/icons';
import { SubjectConfigurationCategoryForm } from '../SubjectConfigurationCategoryForm';
import { SubjectResponse } from '../../../dto/subject';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks';

interface SubjectConfigurationProps {
  subject: SubjectResponse;
}

export const SubjectConfiguration = (
  { subject }: SubjectConfigurationProps,
): JSX.Element => {
  const {
    data,
    loading,
    error,
    reload,
  } = useFetch<GradeCategoriesResponse>(`/api/grade/category?subjectId=${subject.id}`);

  const [createGradeCategoryModalOpened, createGradeCategoryModalHandlers] = useDisclosure(false);

  return (
    <>
      <Modal
        onClose={createGradeCategoryModalHandlers.close}
        opened={createGradeCategoryModalOpened}
        size={'lg'}
        title={`Tworzenie kategorii oceny dla: ${subject.name} (${subject.course.code}, ${subject.course.schoolYear})`}
      >
        <SubjectConfigurationCategoryForm
          subject={subject}
          onSubmit={() => {
            createGradeCategoryModalHandlers.close();
            reload();
          }}/>
      </Modal>
      <div className={'subject-configuration'}>
        <div className={'subject-configuration-section-head'}>
          <Title order={3}>
            Kategorie ocen
          </Title>
          <Button
            leftIcon={<IconPlus size={18}/>}
            variant={'default'}
            onClick={createGradeCategoryModalHandlers.open}
          >
            Dodaj kategorię
          </Button>
        </div>
        {data && (
          <Table striped>
            <thead>
            <tr>
              <th>#</th>
              <th>Nazwa kategorii</th>
              <th>Domyślna waga</th>
              <th>Kolor</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            {data.categories.map((category: GradeCategoriesResponseEntry, idx: number) =>
              (
                <tr key={category.id}>
                  <td>{idx + 1}</td>
                  <td>{category.name}</td>
                  <td>{category.defaultWeight}</td>
                  <td>{category.color ? <ColorSwatch color={category.color} size={20}/> : '–'}</td>
                  <td><ActionIcon><IconPencil size={16}/></ActionIcon></td>
                </tr>
              ))}
            </tbody>
          </Table>)}
      </div>
    </>
  );
};