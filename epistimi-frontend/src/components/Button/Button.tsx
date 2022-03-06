import './Button.scss';
import { MouseEvent, ReactChild } from 'react';

export interface ButtonProps {
  children: ReactChild;
  style?: ButtonStyle;
  disabled?: boolean;
  onClick?: (event: MouseEvent) => void;
  icon?: JSX.Element;
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
      {props.icon}
      {props.children}
    </button>
  );
};
