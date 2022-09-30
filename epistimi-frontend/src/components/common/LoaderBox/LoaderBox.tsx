import './LoaderBox.scss';
import { Loader } from '@mantine/core';
import React from 'react';

export const LoaderBox = (): JSX.Element => {
  return (
    <div className={'loader-box'}>
      <Loader />
    </div>
  );
};