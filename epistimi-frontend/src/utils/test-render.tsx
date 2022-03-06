import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { store as realStore } from '../store/config';
import { Store } from '@reduxjs/toolkit';
import { render as tlRender } from '@testing-library/react';
import React, { ReactChild } from 'react';

export const render = (children: ReactChild, store: Store = realStore) => {
  return tlRender(
    <Provider store={store}>
      <BrowserRouter>
        {children}
      </BrowserRouter>
    </Provider>
  );
};
