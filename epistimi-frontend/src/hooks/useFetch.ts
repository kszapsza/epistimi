import { Dispatch, useEffect, useState } from 'react';
import axios, { AxiosError } from 'axios';

interface FetchResponse<T> {
  data?: T,
  setData: Dispatch<T>;
  loading: boolean,
  error?: AxiosError,
}

export const useFetch = <T = unknown>(url: string): FetchResponse<T> => {
  const [data, setData] = useState<T>();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<AxiosError>();

  useEffect(() => {
    let unmounted = false;

    !data && axios.get<T>(url)
      .then(({ data }): void => {
        if (unmounted) return;
        setData(data);
        setLoading(false);
      })
      .catch((error: AxiosError): void => {
        if (unmounted) return;
        setError(error);
      })
      .finally((): void => {
        if (unmounted) return;
        setLoading(false);
      });

    return (): void => {
      unmounted = true;
    };
  }, [url]);

  return {
    data,
    setData,
    loading,
    error,
  };
};
