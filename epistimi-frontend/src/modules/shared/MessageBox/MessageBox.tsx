import { ReactElement } from 'react';
import './MessageBox.scss';

export interface MessageBoxProps {
  children: ReactElement | string;
  style?: MessageBoxStyle;
  icon?: ReactElement;
  small?: boolean;
  hidden?: boolean;
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
    (props.small ? 'mbox-small' : '')
  ].join(' ');

  return (
    <div
      className={mboxClasses}
      style={{ visibility: (props.hidden ? 'hidden' : 'visible') }}
    >
      {props.icon && <div className="mbox-icon">
        {props.icon}
      </div>}
      <div className="mbox-text">
        {props.children}
      </div>
    </div>
  );
};
