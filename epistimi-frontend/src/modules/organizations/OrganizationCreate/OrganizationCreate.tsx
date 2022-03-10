import { OrganizationRegisterRequest } from '../../../dto/organization';
import { useForm } from 'react-hook-form';

export const OrganizationCreate = (): JSX.Element => {
  const { formState: { errors }, handleSubmit, register } = useForm<OrganizationRegisterRequest>();

  return (
    <div className={'organization-create'}>
      <h3>Tworzenie nowej placówki</h3>

      <form>
        <input/>
        <input/>
      </form>
    </div>
  );
};

/*
 * TODO: MVP flow:
 *  1) EPISTIMI ADMIN inserts organization data (name, later: address, phone, some formal info, school director)
 *  2) EPISTIMI ADMIN defines orgabization administrator (select from list of ORGANIZATION_ADMIN account, or create a new one)
 *  3) all the rest is configured later by organization admin (staff, courses/classes)
 */

