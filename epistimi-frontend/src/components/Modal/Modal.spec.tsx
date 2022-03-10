import { fireEvent, render } from '@testing-library/react';
import { Modal } from './Modal';

describe('Modal component', () => {
  it('should not render modal if \'open\' if false', () => {
    const { queryByRole } = render(
      <Modal open={false} onClose={jest.fn()}>
        <h3>foo</h3>
      </Modal>,
    );
    expect(queryByRole('heading')).toBeNull();
  });

  it('should render modal and its content if \'open\' if false', () => {
    const { queryByRole } = render(
      <Modal open={true} onClose={jest.fn()}>
        <h3>foo</h3>
      </Modal>,
    );
    expect(queryByRole('heading')).toBeInTheDocument();
  });

  it('should call onClose handler on \'X\' button click', () => {
    const onCloseMock = jest.fn();
    const { getByLabelText } = render(
      <Modal open={true} onClose={onCloseMock}>
        <h3>foo</h3>
      </Modal>,
    );
    expect(onCloseMock).not.toBeCalled();

    getByLabelText('Close').click();
    expect(onCloseMock).toHaveBeenCalledTimes(1);
  });

  it('should call onClose handler when clicked outside modal', () => {
    const onCloseMock = jest.fn();
    const { getByTestId } = render(
      <>
        <div data-testid={'outside'}/>
        <Modal open={true} onClose={onCloseMock}/>
      </>,
    );
    expect(onCloseMock).not.toBeCalled();

    const outsideDiv = getByTestId('outside');
    fireEvent.mouseDown(outsideDiv);

    expect(onCloseMock).toHaveBeenCalledTimes(1);
  });

  it('should not call onClose handler when clicked inside modal', () => {
    const onCloseMock = jest.fn();
    const { getByRole } = render(
      <Modal open={true} onClose={onCloseMock}>
        <h3>foo</h3>
      </Modal>,
    );
    expect(onCloseMock).not.toBeCalled();

    const modal = getByRole('heading');
    fireEvent.mouseDown(modal);

    expect(onCloseMock).not.toBeCalled();
  });
});
