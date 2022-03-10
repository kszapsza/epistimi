import { RefObject, useEffect } from 'react';

interface UseClickOutsideProps {
  ref: RefObject<HTMLElement>;
  handler: () => void;
}

export const useClickOutside = ({ handler, ref }: UseClickOutsideProps): void => {
  useEffect(() => {
    const onClickOutside = (e: Event): void => {
      if (ref.current
        && !ref.current.contains(e.target as HTMLElement)
      ) {
        handler();
      }
    };
    document.addEventListener('mousedown', onClickOutside);
    return (): void => document.removeEventListener('mousedown', onClickOutside);
  }, [handler]);
};
