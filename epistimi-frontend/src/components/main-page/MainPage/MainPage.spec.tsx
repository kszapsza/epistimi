import { MainPage } from './MainPage';
import { render } from '../../../utils/test-render';

describe('MainPage component', () => {

  const WELCOME_TO_EPISTIMI_REGEXP = /mainPage\.welcomeToEpistimi/;

  it('should set page title', () => {
    render(<MainPage/>);
    expect(document.title).toBe('Epistimi');
  });

  it('should render heading and description', () => {
    const { queryByText } = render(<MainPage/>);
    expect(queryByText(WELCOME_TO_EPISTIMI_REGEXP)).toBeInTheDocument();
  });
});
