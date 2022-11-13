import './Subject.scss';
import { Badge, Tabs } from '@mantine/core';
import { IconSpeakerphone, IconStar, IconWriting } from '@tabler/icons';

export const Subject = (): JSX.Element => {
  return (
    <>
      <div className={'subject-head'}>
        <div className={'subject-course'}>
          6a (2012/2013) ∙ Geller Wiktor
        </div>
        <div className={'subject-name'}>
          Język polski
        </div>
      </div>

      <Tabs defaultValue={'grades'}>
        <Tabs.List>
          <Tabs.Tab value={'grades'} icon={<IconStar size={14}/>} rightSection={
            <Badge
              sx={{ width: 16, height: 16, pointerEvents: 'none' }}
              variant={'filled'}
              size={'xs'}
              color={'red'}
              p={0}
            >
              4
            </Badge>
          }>Oceny</Tabs.Tab>
          <Tabs.Tab value={'feed'} icon={<IconSpeakerphone size={14}/>}>Aktualności</Tabs.Tab>
          <Tabs.Tab value={'homework'} icon={<IconWriting size={14}/>} rightSection={
            <Badge
              sx={{ width: 16, height: 16, pointerEvents: 'none' }}
              variant={'filled'}
              size={'xs'}
              color={'red'}
              p={0}
            >
              6
            </Badge>
          }>Zadania</Tabs.Tab>
        </Tabs.List>

        <Tabs.Panel value={'grades'} p={'md'}>Twoja średnia to 1,00</Tabs.Panel>
        <Tabs.Panel value={'feed'} p={'md'}>Wy nieuki!</Tabs.Panel>
        <Tabs.Panel value={'homework'} p={'md'}>Brak zadanek</Tabs.Panel>
      </Tabs>
    </>
  );
};

/*
 * TODO: Tu trzeba wziąć pod uwagę, że do tego widoku ma mieć wgląd każdy
 *  (no, za wyjątkiem EPISTIMI_ADMIN):
 *
 *  - ORGANIZATION_ADMIN/TEACHER:
 *     - tab GRADES: jeśli prowadzi przedmiot, wystaw i przejrzyj oceny, jeśli nie, to dostęp do przedmiotu zabroniony,
 *     - tab FEED: dodawaj aktualności, reaguj, komentuj,
 *     - tab HOMEWORK: dodawaj i sprawdzaj zadania domowe,
 *
 *  - STUDENT/PARENT:
 *     - tab GRADES: przejrzyj oceny swoje lub swojego kida,
 *     - tab FEED: przeglądaj aktualności od nauczyciela, reaguj, komentuj,
 *     - tab HOMEWORK: przejrzyj zadania domowe swojego dziecka (rodzic) lub rozwiązuj (kid).
 */
