import './MessageBox.scss';
import { ReactChild, ReactElement } from 'react';

export interface MessageBoxProps {
  children: ReactChild;
  style?: MessageBoxStyle;
  icon?: ReactElement;
  small?: boolean;
}

export enum MessageBoxStyle {
  CONSTRUCTIVE = 'mbox-constructive',
  INFORMATION = 'mbox-information',
  WARNING = 'mbox-warning'
}

export const MessageBox = (props: MessageBoxProps): JSX.Element => {
  const mboxClasses: string = [
    'mbox',
    (props.style || 'mbox-info'),
    (props.small ? 'mbox-small' : ''),
  ].join(' ');

  return (
    <div className={mboxClasses}>
      {props.icon && <div className="mbox-icon">
        {props.icon}
      </div>}
      <div className="mbox-text">
        {props.children}
      </div>
    </div>
  );
};
