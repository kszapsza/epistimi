import React from 'react';
import ReactDOM from 'react-dom';
import './index.scss';
import { App } from './App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter } from 'react-router-dom';
import axios from 'axios';
// import { store } from './app/store';
// import { Provider } from 'react-redux';

axios.defaults.baseURL = 'http://localhost:8080/api';
axios.defaults.headers.post['Accept'] = 'application/json';
axios.defaults.timeout = 1000;

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <App/>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById('root')
);

// <Provider store={store}>
//  <App />
// </Provider>

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
