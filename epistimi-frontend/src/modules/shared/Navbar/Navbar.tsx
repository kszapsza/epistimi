import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import './Navbar.scss';

export const Navbar = (): JSX.Element => {
  return (
    <header className="navbar">
      <div className="navbar-wrapper">
        <h1 className="navbar-logo">
          Epistimi
        </h1>
        <div className="navbar-user">
          <span className="navbar-user-name">
            Nie zalogowano
          </span>
          <AccountCircleIcon />
        </div>
      </div>
    </header>
  )
}
