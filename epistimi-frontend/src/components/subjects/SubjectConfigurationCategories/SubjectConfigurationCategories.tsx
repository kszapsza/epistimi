import './SubjectConfigurationCategories.scss';
import {
  ActionIcon,
  Alert,
  Button,
  ColorSwatch,
  LoadingOverlay,
  Modal,
  Table,
  Title,
} from '@mantine/core';
import { GradeCategoriesResponse, GradeCategoriesResponseEntry } from '../../../dto/grade-category';
import { IconAlertCircle, IconPencil, IconPlus } from '@tabler/icons';
import {
  SubjectConfigurationCategoryForm,
  SubjectConfigurationCategoryFormMode,
} from '../SubjectConfigurationCategoryForm';
import { SubjectResponse } from '../../../dto/subject';
import { useAppSelector } from '../../../store/hooks';
import { useDisclosure } from '@mantine/hooks';
import { useFetch } from '../../../hooks';
import { UserRole } from '../../../dto/user';
import { useState } from 'react';

interface SubjectConfigurationCategoriesProps {
  subject: SubjectResponse;
}

export const SubjectConfigurationCategories = (
  { subject }: SubjectConfigurationCategoriesProps,
): JSX.Element => {

  const { user } = useAppSelector((state) => state.auth);
  const {
    data,
    loading,
    error,
    reload,
  } = useFetch<GradeCategoriesResponse>(`/api/grade/category?subjectId=${subject.id}`);

  const [createGradeCategoryModalOpened, createGradeCategoryModalHandlers] = useDisclosure(false);
  const [updateGradeCategoryModalOpened, updateGradeCategoryModalHandlers] = useDisclosure(false);
  const [updatedCategory, setUpdatedCategory] = useState<GradeCategoriesResponseEntry>();

  const onCategoryEdit = (category: GradeCategoriesResponseEntry) => {
    setUpdatedCategory(category);
    updateGradeCategoryModalHandlers.open();
  };

  return (
    <>
      <Modal
        onClose={createGradeCategoryModalHandlers.close}
        opened={createGradeCategoryModalOpened}
        size={'lg'}
        title={`Tworzenie kategorii oceny: ${subject.name} (${subject.course.code}, ${subject.course.schoolYear})`}
      >
        <SubjectConfigurationCategoryForm
          subject={subject}
          onSubmit={() => {
            createGradeCategoryModalHandlers.close();
            reload();
          }}
          mode={SubjectConfigurationCategoryFormMode.CREATE}
        />
      </Modal>
      {updatedCategory && (
        <Modal
          onClose={updateGradeCategoryModalHandlers.close}
          opened={updateGradeCategoryModalOpened}
          size={'lg'}
          title={`Edytowanie kategorii oceny: ${updatedCategory.name}`}
        >
          <SubjectConfigurationCategoryForm
            subject={subject}
            onSubmit={() => {
              updateGradeCategoryModalHandlers.close();
              reload();
            }}
            mode={SubjectConfigurationCategoryFormMode.UPDATE}
            existingCategory={updatedCategory}
          />
        </Modal>
      )}

      <LoadingOverlay visible={loading}/>

      {error &&
        <Alert icon={<IconAlertCircle size={16}/>} color={'red'} title={'Błąd'}>
          Nie udało się załadować listy kategorii ocen
        </Alert>}

      <div className={'subject-configuration-section-head'}>
        <Title order={3}>
          Kategorie ocen
        </Title>
        <Button
          disabled={!user || user.role != UserRole.TEACHER}
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
                <td>
                  <ActionIcon
                    onClick={() => onCategoryEdit(category)}
                    disabled={!user || user.role != UserRole.TEACHER}
                  >
                    <IconPencil size={16}/>
                  </ActionIcon>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>)}
    </>
  );
};

