import './CourseDetailsSection.scss';
import { Badge, Title } from '@mantine/core';

export interface CourseDetailsSectionProps {
  title: string;
  badge?: number;
  actionButton?: JSX.Element;
  content: JSX.Element;
}

export const CourseDetailsSection = (
  { actionButton, content, badge, title }: CourseDetailsSectionProps,
): JSX.Element => {
  return (
    <div className={'course-details-section'}>
      <div className={'course-details-section-head'}>
        <div className={'course-details-section-heading'}>
          <Title order={3}>{title}</Title>
          {badge && <Badge variant={'filled'} size={'sm'}>{badge}</Badge>}
        </div>
        <div className={'course-details-section-action'}>
          {actionButton}
        </div>
      </div>
      {content}
    </div>
  );
};
