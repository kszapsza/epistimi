import { MouseEvent, ReactChild } from 'react';
import './Button.scss';

export interface ButtonProps {
  children: ReactChild;
  style?: ButtonStyle;
  disabled?: boolean;
  onClick?: (event: MouseEvent) => void;
}

export enum ButtonStyle {
  LIGHT = 'btn',
  DARK = 'btn-dark',
  PRIMARY = 'btn-primary',
  CONSTRUCTIVE = 'btn-constructive',
  DESTRUCTIVE = 'btn-destructive'
}

export const Button = (props: ButtonProps): JSX.Element => {
  return (
    <button
      className={props.style?.toString() || 'btn'}
      disabled={props.disabled}
      onClick={props.onClick}
    >
      {props.children}
    </button>
  )
}
