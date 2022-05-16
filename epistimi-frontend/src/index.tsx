import './index.scss';
import * as serviceWorker from './serviceWorker';
import { App } from './App';
import { BrowserRouter } from 'react-router-dom';
import { PersistGate } from 'redux-persist/integration/react';
import { persistor, store } from './store/config';
import { Provider } from 'react-redux';
import { TOKEN_KEY } from './store/slices/authSlice';
import axios from 'axios';
import dayjs from 'dayjs';
import React from 'react';
import ReactDOM from 'react-dom';

axios.defaults.baseURL = 'http://localhost:8080/';
// axios.defaults.timeout = 1000; TODO

axios.interceptors.request.use(
  async (config) => {
    if (config.headers) {
      const token = localStorage.getItem(TOKEN_KEY) ?? null;
      token && (config.headers['Authorization'] = `Bearer ${token}`);
      config.headers.Accept = 'application/vnd.epistimi.api.v1+json';
    }
    return config;
  },
);

dayjs.locale('pl');

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <BrowserRouter>
          <App/>
        </BrowserRouter>
      </PersistGate>
    </Provider>
  </React.StrictMode>,
  document.getElementById('root'),
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
