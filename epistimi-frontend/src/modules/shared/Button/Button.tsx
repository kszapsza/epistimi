import './Button.scss';
import { ReactChild } from 'react';

export interface ButtonProps {
  children: ReactChild;
  style?: ButtonStyle;
}

export enum ButtonStyle {
  LIGHT = 'btn',
  DARK = 'btn-dark',
  CONSTRUCTIVE = 'btn-constructive',
  DESTRUCTIVE = 'btn-destructive'
}

export const Button = (props: ButtonProps): JSX.Element => {
  return (
    <button className={props.style?.toString() || 'btn'}>
      {props.children}
    </button>
  )
}
