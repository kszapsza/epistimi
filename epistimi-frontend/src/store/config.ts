import storage from 'redux-persist/lib/storage';
import { Action, combineReducers, configureStore, ThunkAction } from '@reduxjs/toolkit';
import { currentUserReducer } from './slices/authSlice';
import { persistReducer, persistStore } from 'redux-persist'
import { PERSIST, REGISTER } from 'redux-persist/es/constants';

const rootReducer = combineReducers({
  auth: currentUserReducer,
});

const persistConfig = {
  key: 'root',
  storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== 'production',
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [PERSIST, REGISTER],
      },
    }),
});

export const persistor = persistStore(store);

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<ReturnType, RootState, unknown, Action<string>>;
