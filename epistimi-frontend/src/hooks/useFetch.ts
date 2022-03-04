import { useEffect, useState } from 'react';
import axios, { AxiosError } from 'axios';

interface FetchResponse<T> {
  data?: T,
  loading: boolean,
  error?: AxiosError,
}

export const useFetch = <T = unknown>(url: string): FetchResponse<T> => {
  const [data, setData] = useState<T>();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<AxiosError>();

  useEffect(() => {
    let unmounted = false;

    axios.get<T>(url)
      .then(({ data }) => {
        if (unmounted) return;
        setData(data);
        setLoading(false);
      })
      .catch((error: AxiosError) => {
        if (unmounted) return;
        setError(error);
      })
      .finally(() => {
        if (unmounted) return;
        setLoading(false);
      });

    return () => {
      unmounted = true;
    };
  }, [url]);

  return { data, loading, error };
};
