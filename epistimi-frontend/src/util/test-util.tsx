import { ReactElement } from 'react';
import { render } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';

export const renderWithRouter = (element: ReactElement) => {
  return (
    render(
      <BrowserRouter>
        {element}
      </BrowserRouter>
    )
  );
};
