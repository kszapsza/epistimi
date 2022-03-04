import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store } from '../store/config';
import { render as tlRender } from '@testing-library/react';
import React, { ReactChild } from 'react';

export const render = (children: ReactChild) => {
  return tlRender(
    <Provider store={store}>
      <BrowserRouter>
        {children}
      </BrowserRouter>
    </Provider>
  );
};
