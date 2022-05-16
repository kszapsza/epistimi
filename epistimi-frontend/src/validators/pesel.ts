export const validatePesel = (pesel: string): boolean => {
  const peselDigits = peselToDigitArray(pesel);
  return peselDigits.length === 11
    && 10 - calculateControlSum(peselDigits) % 10 === peselDigits[10];
};

const peselToDigitArray = (pesel: string): number[] => {
  return pesel
    .split('')
    .map(char => Number(char))
    .filter(digit => !Number.isNaN(digit));
};

const calculateControlSum = (peselDigits: number[]): number => {
  const peselWeights = [1, 3, 7, 9, 1, 3, 7, 9, 1, 3];
  return peselDigits
    .slice(0, 10)
    .reduce((prev, next, idx) => {
      return prev + next * peselWeights[idx];
    }, 0);
};
