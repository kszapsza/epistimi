import './Button.scss';

interface ButtonProps {
  children: JSX.Element | string;
  style?: ButtonStyle;
}

export enum ButtonStyle {
  LIGHT = 'btn',
  DARK = 'btn-dark',
  CONSTRUCTIVE = 'btn-constructive',
  DESTRUCTIVE = 'btn-destructive'
}

const Button = (props: ButtonProps): JSX.Element => {
  return (
    <button className={props.style?.toString() || 'btn'}>
      {props.children}
    </button>
  )
}

export default Button;
