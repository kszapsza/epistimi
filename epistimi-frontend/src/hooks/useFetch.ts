import { Reducer, useEffect, useReducer } from 'react';
import axios, { AxiosError } from 'axios';

type FetchResponse<TData> = {
  data?: TData;
  loading: boolean;
  error?: AxiosError;
  reload: () => void;
};

type FetchState<TData> = {
  data?: TData;
  loading: boolean;
  error?: AxiosError;
  reloadFlag: boolean;
};

type FetchAction<TData> =
  | { type: 'FETCH_LOADING' }
  | { type: 'FETCH_RELOADING' }
  | { type: 'FETCH_SUCCESS', data: TData }
  | { type: 'FETCH_ERROR', error: AxiosError };

type FetchReducer<TData> = Reducer<FetchState<TData>, FetchAction<TData>>;

const reducer = <TData>(
  state: FetchState<TData>,
  action: FetchAction<TData>,
): FetchState<TData> => {
  switch (action.type) {
    case 'FETCH_LOADING':
      return {
        ...state,
        error: undefined,
        loading: true,
      };
    case 'FETCH_SUCCESS':
      return {
        ...state,
        error: undefined,
        data: action.data,
        loading: false,
      };
    case 'FETCH_ERROR':
      return {
        ...state,
        error: action.error,
        loading: false,
      };
    case 'FETCH_RELOADING':
      return {
        ...state,
        reloadFlag: !state.reloadFlag,
        error: undefined,
        loading: true,
      };
  }
};

export const useFetch = <TData = unknown>(url: string): FetchResponse<TData> => {
  const [state, dispatch] = useReducer<FetchReducer<TData>>(reducer, {
    loading: false,
    reloadFlag: false,
  });

  useEffect(() => {
    let unmounted = false;
    dispatch({ type: 'FETCH_LOADING' });

    axios.get<TData>(url)
      .then(({ data }): void => {
        if (unmounted) return;
        dispatch({ type: 'FETCH_SUCCESS', data });
      })
      .catch((error: AxiosError): void => {
        if (unmounted) return;
        dispatch({ type: 'FETCH_ERROR', error });
      })
      .finally((): void => {
        if (unmounted) return;
      });

    return (): void => {
      unmounted = true;
    };
  }, [url, state.reloadFlag]);

  return {
    ...state,
    reload: () => dispatch({ type: 'FETCH_RELOADING' }),
  };
};
