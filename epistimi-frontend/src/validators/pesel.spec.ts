import { validatePesel } from './pesel';

describe('PESEL validator', () => {
  it('should return false for too short PESEL', () => {
    // given
    const pesel = '123';

    // when
    const result = validatePesel(pesel);

    // then
    expect(result).toBe(false);
  });

  it('should return false for too long PESEL', () => {
    // given
    const pesel = '123456789123456789';

    // when
    const result = validatePesel(pesel);

    // then
    expect(result).toBe(false);
  });

  it.each([
    '0628232a269',
    '06282328@69',
    '062#232a269',
    '062823282.9',
    '#6282328269',
  ])('should return false for PESEL with symbols other than digits', (pesel: string) => {
    // when
    const result = validatePesel(pesel);

    // then
    expect(result).toBe(false);
  });

  it.each([
    '67032772556',
    '65052622439',
    '55111863352',
    '06282328269',
    '77031851792',
    '03272082831',
    '51050672187',
    '71072917855',
    '68070824232',
    '89062379994',
  ])('should return true for valid PESEL', (pesel: string) => {
    // when
    const result = validatePesel(pesel);

    // then
    expect(result).toBe(true);
  });

  it.each([
    '67032772557',
    '65052622430',
    '55111863353',
    '06282328260',
    '77031851791',
    '03272082830',
    '51050672186',
    '71072917854',
    '68070824231',
    '89062379995',
  ])('should return false for invalid PESEL', (pesel: string) => {
    // when
    const result = validatePesel(pesel);

    // then
    expect(result).toBe(false);
  });
});
