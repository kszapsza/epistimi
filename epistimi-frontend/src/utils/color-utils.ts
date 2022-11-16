interface RgbColor {
  red: number;
  green: number;
  blue: number;
}

const expandShortHexCode = (shortHex: string): string => {
  return shortHex.replace(
    /^#?([a-f\d])([a-f\d])([a-f\d])$/i,
    (_, red, green, blue) => {
      return `#${red}${red}${green}${green}${blue}${blue}`;
    });
};

export const hexToRgb = (hex: string): RgbColor => {
  // expand #fff etc. and drop "#"
  const expandedHex = expandShortHexCode(hex).slice(1);
  const hexValue = parseInt(expandedHex, 16);
  return {
    red: (hexValue >> 16) & 255,
    green: (hexValue >> 8) & 255,
    blue: (hexValue) & 255,
  };
};

export const determineTextColor = (hexColorCode: string): string => {
  const { red, green, blue } = hexToRgb(hexColorCode);
  if (red * 0.299 + green * 0.587 + blue * 0.114 > 186) {
    return '#2c2e33';
  } else {
    return '#ffffff';
  }
};