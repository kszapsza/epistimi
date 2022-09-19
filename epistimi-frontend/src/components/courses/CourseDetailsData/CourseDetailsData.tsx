import './CourseDetailsData.scss';
import { CourseDetailsKeyValue } from '../../courses';
import { CourseResponse } from '../../../dto/course';
import { UserAvatar } from '../../common';
import { useTranslation } from 'react-i18next';
import dayjs from 'dayjs';

interface CourseDetailsDataProps {
  course: CourseResponse;
}

export const CourseDetailsData = (
  { course }: CourseDetailsDataProps,
): JSX.Element => {
  const DATE_FORMAT = 'D MMMM YYYY';
  const { t } = useTranslation();

  return (
    <div className={'course-details-data'}>
      <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.classTeacher')}
        value={
          <div className={'course-class-teacher'}>
            <UserAvatar user={course.classTeacher.user} size={'xs'} radius={'xl'}/>
            <a href={`/app/teachers/${course.classTeacher.id}`}>
              {course.classTeacher.user.firstName} {course.classTeacher.user.lastName}
            </a>
          </div>
        }/>

      {course.profile && <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.profile')}
        value={course.profile}/>}
      {course.profession && <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.profession')}
        value={course.profession}/>}
      {course.specialization && <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.specialization')}
        value={course.specialization}/>}

      <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.schoolYearBegin')}
        value={dayjs(course.schoolYearBegin).format(DATE_FORMAT)}/>
      <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.schoolYearSemesterEnd')}
        value={dayjs(course.schoolYearSemesterEnd).format(DATE_FORMAT)}/>
      <CourseDetailsKeyValue
        label={t('courses.courseDetailsData.schoolYearEnd')}
        value={dayjs(course.schoolYearEnd).format(DATE_FORMAT)}/>
    </div>
  );
};
