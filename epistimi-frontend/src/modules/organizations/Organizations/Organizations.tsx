import './Organizations.scss';
import { Button, MessageBox, MessageBoxStyle, Spinner } from '../../../components';
import { Create, ErrorOutline } from '@mui/icons-material';
import { OrganizationsResponse, OrganizationStatus } from '../../../dto/organization';
import { useFetch } from '../../../hooks/useFetch';

export const Organizations = (): JSX.Element => {
  const { data, loading, error } = useFetch<OrganizationsResponse>('api/organization');

  const activeCount: number = data?.organizations.filter((organization) => {
    return organization.status == OrganizationStatus.ENABLED;
  }).length ?? 0;

  return (
    <div className={'organizations'}>
      <div className={'organizations-actions'}>
        <h2>Placówki</h2>
        <Button
          icon={<Create/>}
        >
          Zarejestruj nową
        </Button>
      </div>

      {error &&
        <MessageBox style={MessageBoxStyle.WARNING} icon={<ErrorOutline/>}>
          Nie udało się załadować listy placówek!
        </MessageBox>}
      {loading && <Spinner/>}

      {data &&
        <>
          <table className={'organizations-listing'}>
            <tbody>
            <tr>
              <th>Id</th>
              <th>Nazwa</th>
              <th>Administrator</th>
              <th>Status</th>
            </tr>
            {data.organizations.map(({ id, name, admin, status }) =>
              <tr key={id}>
                <td>{id}</td>
                <td>{name}</td>
                <td>{admin.username}</td>
                <td>{status.toString()}</td>
              </tr>,
            )}
            </tbody>
          </table>
          <p>Łącznie: {data.organizations.length}, w tym aktywnych: {activeCount}.</p>
        </>}
    </div>
  );
};
