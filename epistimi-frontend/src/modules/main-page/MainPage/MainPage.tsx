import './MainPage.scss';
import ArticleListing from '../ArticleListing';
import LoginForm from '../LoginForm';

const MainPage = (): JSX.Element => {
  return (
    <div className={'main-page'}>
      <div className={'main-page-articles'}>
        <ArticleListing/>
      </div>
      <div className={'main-page-login'}>
        <LoginForm/>
      </div>
    </div>
  )
}

export default MainPage;
