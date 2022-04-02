import { CourseResponse } from '../../../dto/course';
import { CoursesListingTile } from '../CoursesListingTile';

interface CoursesListingGroupProps {
  courses: CourseResponse[];
}

export const CoursesListingGroup = ({ courses }: CoursesListingGroupProps): JSX.Element => {
  return (
    <div className={'courses-listing'}>
      {courses
        .sort(({ code: codeA }, { code: codeB }) =>
          `${codeA.number}${codeA.letter}`.localeCompare(`${codeB.number}${codeB.letter}`, 'pl-PL'),
        )
        .map((course) =>
          <CoursesListingTile
            key={course.id}
            code={course.code}
            classTeacher={course.classTeacher}
            studentsCount={course.students.length}
          />,
        )}
    </div>
  );
};
