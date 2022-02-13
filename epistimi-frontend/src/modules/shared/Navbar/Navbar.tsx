import NoAccountsIcon from '@mui/icons-material/NoAccounts';
import './Navbar.scss';
import { Link } from 'react-router-dom';

export const Navbar = (): JSX.Element => {
  return (
    <header className="navbar">
      <div className="navbar-wrapper">
        <h1 className="navbar-logo">
          <Link to="/">Epistimi</Link>
        </h1>
        <div className="navbar-user">
          <span className="navbar-user-name">
            Nie zalogowano
          </span>
          <NoAccountsIcon/>
        </div>
      </div>
    </header>
  )
}
