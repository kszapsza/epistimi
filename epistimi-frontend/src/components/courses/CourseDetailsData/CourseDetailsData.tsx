import './CourseDetailsData.scss';
import { CourseDetailsKeyValue } from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { UserAvatar } from '../../common';
import dayjs from 'dayjs';

interface CourseDetailsDataProps {
  course: CourseResponse;
}

export const CourseDetailsData = (
  { course }: CourseDetailsDataProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY';

  return (
    <div className={'course-details-data'}>
      <CourseDetailsKeyValue
        label={'Wychowawca klasy'}
        value={
          <div className={'course-class-teacher'}>
            <UserAvatar user={course.classTeacher.user} size={'xs'} radius={'xl'}/>
            <a href={`/app/teachers/${course.classTeacher.id}`}>
              {course.classTeacher.user.firstName} {course.classTeacher.user.lastName}
            </a>
          </div>
        }/>

      {course.profile && <CourseDetailsKeyValue
        label={'Profil'}
        value={course.profile}/>}
      {course.profession && <CourseDetailsKeyValue
        label={'Zawód'}
        value={course.profession}/>}
      {course.specialization && <CourseDetailsKeyValue
        label={'Specjalizacja'}
        value={course.specialization}/>}

      <CourseDetailsKeyValue
        label={'Rozpoczęcie roku'}
        value={dayjs(course.schoolYearBegin).format(DATE_FORMAT)}/>
      <CourseDetailsKeyValue
        label={'Koniec pierwszego semestru'}
        value={dayjs(course.schoolYearSemesterEnd).format(DATE_FORMAT)}/>
      <CourseDetailsKeyValue
        label={'Koniec roku'}
        value={dayjs(course.schoolYearEnd).format(DATE_FORMAT)}/>
    </div>
  );
};
