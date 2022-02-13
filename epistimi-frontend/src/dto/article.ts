export interface Article {
  id: string;
  title: string;
  description: string;
}

export type Articles = { articles: Article[] };
