import './i18n';
import './index.scss';
import * as serviceWorker from './serviceWorker';
import { App } from './App';
import { BrowserRouter } from 'react-router-dom';
import { LoaderBox } from './components/common';
import { PersistGate } from 'redux-persist/integration/react';
import { persistor, store } from './store/config';
import { Provider } from 'react-redux';
import { TOKEN_KEY } from './store/slices/authSlice';
import axios from 'axios';
import dayjs from 'dayjs';
import localizedFormat from 'dayjs/plugin/localizedFormat';
import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import relativeTime from 'dayjs/plugin/relativeTime';

axios.defaults.baseURL = 'http://localhost:8080/';
axios.defaults.timeout = 1500;

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

dayjs.extend(localizedFormat).locale('pl');
dayjs.extend(relativeTime).locale('pl');

ReactDOM.render(
  <React.StrictMode>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <BrowserRouter>
          <Suspense fallback={<LoaderBox/>}>
            <App/>
          </Suspense>
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
