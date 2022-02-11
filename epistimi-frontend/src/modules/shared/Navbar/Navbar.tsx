import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import './Navbar.scss';

const Navbar = (): JSX.Element => {
  return (
    <div className={'navbar'}>
      <div className={'navbar-wrapper'}>
        <h1 className={'navbar-logo'}>
          Epistimi
        </h1>
        <div className={'navbar-user'}>
          <span className={'navbar-user-name'}>
            Zaloguj się
          </span>
          <AccountCircleIcon />
        </div>
      </div>
    </div>
  )
}

export default Navbar;
