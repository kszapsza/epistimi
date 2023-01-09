import './CourseDetailsTile.scss';
import { Card } from '@mantine/core';
import { Link, useLocation } from 'react-router-dom';

interface CourseDetailsTileProps {
  avatar: JSX.Element;
  title: string;
  subtitle: string;
  href?: string;
}

export const CourseDetailsTile = (
  { avatar, title, subtitle, href }: CourseDetailsTileProps,
): JSX.Element => {
  const location = useLocation();

  return (
    <Card
      withBorder
      className={'course-details-tile'}
      component={Link}
      to={href ? href : location.pathname}
    >
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
