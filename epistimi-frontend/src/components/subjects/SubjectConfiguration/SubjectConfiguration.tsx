import './SubjectConfiguration.scss';
import { SubjectConfigurationCategories } from '../SubjectConfigurationCategories';
import { SubjectResponse } from '../../../dto/subject';

interface SubjectConfigurationProps {
  subject: SubjectResponse;
}

export const SubjectConfiguration = (
  { subject }: SubjectConfigurationProps,
): JSX.Element => {
  return (
    <>
      <div className={'subject-configuration'}>
        <SubjectConfigurationCategories subject={subject}/>
      </div>
    </>
  );
};