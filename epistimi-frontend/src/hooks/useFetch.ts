import { useEffect, useState } from 'react';
import axios, { AxiosError } from 'axios';

interface FetchResponse<T> {
  data?: T,
  loading: boolean,
  error?: any,
}

export const useFetch = <T>(url: string): FetchResponse<T> => {
  const [data, setData] = useState<T>();
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<AxiosError>();

  useEffect(() => {
    axios.get<T>(url)
      .then(({ data }) => {
        setData(data);
        setLoading(false);
      })
      .catch((error: AxiosError) => {
        setError(error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [url]);

  return { data, loading, error };
};