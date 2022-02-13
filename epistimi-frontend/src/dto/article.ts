export interface Article {
  id: string;
  slug: string;
  title: string;
  description: string;
}

export type Articles = { articles: Article[] };
