import { useLayoutEffect } from 'react';

/**
 * Changes document title to a string provided in `title` parameter,
 * with "– Epistimi" suffix automatically appended. If provided "title" is undefined
 * or an empty string, `title` will be set to "Epistimi" alone.
 *
 * @param title text to be set as document title, along with "– Epistimi" suffix.
 */
export const useDocumentTitle = (title?: string): void => {
  useLayoutEffect(() => {
    if (!document) {
      return;
    }
    if (!title) {
      document.title = 'Epistimi';
      return;
    }
    document.title = `${title} – Epistimi`;
  }, [title]);
};
