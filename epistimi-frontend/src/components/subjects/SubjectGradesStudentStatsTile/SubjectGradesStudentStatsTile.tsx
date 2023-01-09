import { createStyles, Group, Paper, Text } from '@mantine/core';

const useStyles = createStyles((theme) => ({
  value: {
    fontSize: 24,
    fontWeight: 700,
    lineHeight: 1,
  },
  icon: {
    color: theme.colorScheme === 'dark' ? theme.colors.dark[3] : theme.colors.gray[4],
  },
  title: {
    fontWeight: 700,
    textTransform: 'uppercase',
  },
}));

interface SubjectGradesStudentStatsTileProps {
  title: string;
  value?: string | number | JSX.Element;
  icon?: JSX.Element;
}

export const SubjectGradesStudentStatsTile = (
  { icon, title, value }: SubjectGradesStudentStatsTileProps,
): JSX.Element => {
  const { classes } = useStyles();

  return (
    <Paper withBorder p={'md'} radius={'sm'} key={title}>
      <Group position={'apart'}>
        <Text size={'xs'} color={'dimmed'} className={classes.title}>
          {title}
        </Text>
        {icon}
      </Group>
      <Group align={'flex-end'} spacing={'xs'} mt={5}>
        <Text className={classes.value}>
          {value ?? '—'}
        </Text>
      </Group>
    </Paper>
  );
};

