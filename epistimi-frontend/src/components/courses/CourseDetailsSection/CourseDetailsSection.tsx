import './CourseDetailsSection.scss';
import { Title } from '@mantine/core';

export interface CourseDetailsSectionProps {
  title: string;
  subtitle: number;
  actionButton?: JSX.Element;
  content: JSX.Element;
}

export const CourseDetailsSection = (
  { actionButton, content, subtitle, title }: CourseDetailsSectionProps,
): JSX.Element => {
  return (
    <div className={'course-details-section'}>
      <div className={'course-details-section-head'}>
        <div className={'course-details-section-heading'}>
          <Title order={3}>{title}</Title>
          <div className={'course-details-section-subtitle'}>({subtitle})</div>
        </div>
        <div className={'course-details-section-action'}>
          {actionButton}
        </div>
      </div>
      {content}
    </div>
  );
};
