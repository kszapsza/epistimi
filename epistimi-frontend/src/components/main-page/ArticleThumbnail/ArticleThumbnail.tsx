import './ArticleThumbnail.scss';
import { Badge, Card, Group, Image, Text } from '@mantine/core';
import { Link } from 'react-router-dom';

interface ArticleThumbnailProps {
  id: string;
  slug: string;
  image?: string;
  title: string;
  description: string;
}

export const ArticleThumbnail = (props: ArticleThumbnailProps): JSX.Element => {
  const image = props.image ?? 'article-default.jpg';

  return (
    <Card component={Link} to={`/article/${props.slug}`}
          shadow={'sm'} p={'lg'} className={'article-thumbnail'}>
      <Card.Section>
        <Image src={`${process.env.PUBLIC_URL}/images/${image}`} height={160} alt={props.title}/>
      </Card.Section>

      <Group position={'apart'} mt={'md'} mb={'xs'}>
        <Text weight={600} className={'article-thumbnail-title'} role={'heading'}>
          {props.title}
        </Text>
      </Group>

      <Text size={'sm'} mb={'md'} className={'article-thumbnail-description'} role={'definition'}>
        {props.description}
      </Text>

      <Badge color={'blue'} variant={'light'}>
        Kategoria
      </Badge>
    </Card>
  );
};
