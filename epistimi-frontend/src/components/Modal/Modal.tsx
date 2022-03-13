import './Modal.scss';
import { Close } from '@mui/icons-material';
import { ReactNode, useRef } from 'react';
import { useClickOutside } from '../../hooks/useClickOutside';

interface ModalProps {
  open: boolean;
  onClose: () => void;
  children?: ReactNode;
}

export const Modal = ({ children, onClose, open }: ModalProps): JSX.Element => {
  const modalRef = useRef<HTMLDivElement>(null);

  useClickOutside({
    handler: onClose,
    ref: modalRef,
  });

  // TODO: [Esc] click closes modal

  if (!open) {
    return (<></>);
  }

  return (
    <div className={'modal-container'}>
      <div className={'modal'} ref={modalRef}>
        <div className={'modal-close'} onClick={onClose} role={'button'} aria-label={'Close'}>
          <Close/>
        </div>
        {children}
      </div>
    </div>
  );
};
