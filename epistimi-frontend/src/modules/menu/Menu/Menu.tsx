import './Menu.scss';
import { Footer } from '../../../components/Footer';
import { MenuEpistimiAdmin } from '../MenuEpistimiAdmin';
import { MenuOrganizationAdmin } from '../MenuOrganizationAdmin';
import { MenuParent } from '../MenuParent';
import { MenuStudent } from '../MenuStudent';
import { MenuTeacher } from '../MenuTeacher';
import { Outlet } from 'react-router';
import { useAppSelector } from '../../../store/hooks';
import { UserRole } from '../../../dto/user';

export const Menu = (): JSX.Element => {
  const { user } = useAppSelector((state) => state.auth);

  let menu: JSX.Element;

  switch (user?.role) {
    case UserRole.EPISTIMI_ADMIN:
      menu = <MenuEpistimiAdmin/>;
      break;
    case UserRole.ORGANIZATION_ADMIN:
      menu = <MenuOrganizationAdmin/>;
      break;
    case UserRole.TEACHER:
      menu = <MenuTeacher/>;
      break;
    case UserRole.STUDENT:
      menu = <MenuStudent/>;
      break;
    case UserRole.PARENT:
      menu = <MenuParent/>;
      break;
    default:
      menu = <></>;
  }

  return (
    <>
      <div className={'menu-frame'}>
        <ul className={'menu'}>
          {menu}
        </ul>
        <Outlet/>
      </div>
      <Footer/>
    </>
  );
};
