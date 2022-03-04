import { store } from '../store/config';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import React, { ReactChild } from 'react';
import { render as tlRender } from '@testing-library/react';

export const render = (children: ReactChild) => {
  return tlRender(
    <Provider store={store}>
      <BrowserRouter>
        {children}
      </BrowserRouter>
    </Provider>
  );
};
