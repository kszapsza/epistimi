import { MainPage } from './MainPage';
import { render } from '../../../utils/test-render';

describe('MainPage component', () => {
  it('should set page title', () => {
    render(<MainPage/>);
    expect(document.title).toBe('Epistimi');
  });

  it('should render heading and description', () => {
    const { getByRole } = render(<MainPage/>);
    expect(getByRole('heading'))
      .toHaveTextContent('E‑dziennik i platforma e‑learningowa – od teraz w jednym miejscu.');
  });
});
