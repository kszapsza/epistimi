import './CourseDetailsTile.scss';
import { Card } from '@mantine/core';

interface CourseDetailsTileProps {
  avatar: JSX.Element;
  title: string;
  subtitle: string;
  href: string;
}

export const CourseDetailsTile = (
  { avatar, title, subtitle, href }: CourseDetailsTileProps,
): JSX.Element => {
  return (
    <Card className={'course-details-tile'} component={'a'} href={href}>
      {avatar}
      <div className={'course-details-tile-text'}>
        <div className={'course-details-tile-title'}>
          {title}
        </div>
        <div className={'course-details-tile-subtitle'}>
          {subtitle}
        </div>
      </div>
    </Card>
  );
};
