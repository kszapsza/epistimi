import { MainPage } from './MainPage';
import { render } from '../../../utils/test-render';

describe('MainPage component', () => {
  it('should set page title', () => {
    render(<MainPage/>);
    expect(document.title).toBe('Epistimi');
  });

  it('should render heading and description', () => {
    const { queryByText } = render(<MainPage/>);
    expect(queryByText('E‑dziennik i platforma e‑learningowa – od teraz w jednym miejscu.')).toBeInTheDocument();
  });
});
