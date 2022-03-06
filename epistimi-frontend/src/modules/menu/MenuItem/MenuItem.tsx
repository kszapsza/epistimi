import './MenuItem.scss';
import { NavLink } from 'react-router-dom';

interface MenuItemProps {
  icon: JSX.Element;
  label: string;
  href: string;
}

export const MenuItem = ({ icon, label, href }: MenuItemProps): JSX.Element => {
  return (
    <li>
      <NavLink to={href} className={({ isActive }) => isActive ? 'menu-item-active' : 'menu-item'}>
        <div className={'menu-item-content'}>
          <div className={'menu-item-icon'}>{icon}</div>
          <div className={'menu-item-label'}>{label}</div>
        </div>
      </NavLink>
    </li>
  );
};
