import './NoticeboardPostForm.scss';
import { Alert, Button, Textarea, TextInput } from '@mantine/core';
import { IconAlertCircle, IconCheck } from '@tabler/icons';
import { NoticeboardPostRequest, NoticeboardPostResponse } from '../../../dto/noticeboard-post';
import { useForm } from '@mantine/form';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import axios, { AxiosResponse } from 'axios';

export const enum NoticeboardPostFormVariant {
  CREATE,
  UPDATE,
}

interface NoticeboardPostFormCommonProps {
  onSubmit: (response: NoticeboardPostResponse) => void;
  onCancel: () => void;
}

interface NoticeboardPostFormUpdateProps extends NoticeboardPostFormCommonProps {
  variant: NoticeboardPostFormVariant.UPDATE;
  onSubmit: (response: NoticeboardPostResponse) => void;
  post: NoticeboardPostResponse;
}

interface NoticeboardPostFormCreateProps extends NoticeboardPostFormCommonProps {
  variant: NoticeboardPostFormVariant.CREATE;
}

type NoticeboardPostFormProps =
  | NoticeboardPostFormUpdateProps
  | NoticeboardPostFormCreateProps;

export const NoticeboardPostForm = (props: NoticeboardPostFormProps): JSX.Element => {
  const [sendingRequest, setSendingRequest] = useState(false);
  const [error, setError] = useState(false);

  const { t } = useTranslation();

  const form = useForm<NoticeboardPostRequest>({
    initialValues: (
      (props.variant === NoticeboardPostFormVariant.UPDATE)
        ? { title: props.post.title, content: props.post.content }
        : { title: '', content: '' }
    ),
    validate: (values) => ({
      title: values.title.trim() === '' ? t('noticeboard.noticeboardPostForm.requiredField') : null,
      content: values.content.trim() === '' ? t('noticeboard.noticeboardPostForm.requiredField') : null,
    }),
  });

  const handleSubmit = (): void => {
    if (form.validate().hasErrors) {
      return;
    }
    switch (props.variant) {
      case NoticeboardPostFormVariant.CREATE:
        return handleCreate();
      case NoticeboardPostFormVariant.UPDATE:
        return handleUpdate(props.post.id);
    }
  };

  const handleCreate = (): void => {
    setSendingRequest(true);
    axios.post<NoticeboardPostResponse, AxiosResponse<NoticeboardPostResponse>, NoticeboardPostRequest>(
      '/api/noticeboard/post', form.values)
      .then((response) => {
        setSendingRequest(false);
        props.onSubmit(response.data);
      })
      .catch(() => {
        setSendingRequest(false);
        setError(true);
      });
  };

  const handleUpdate = (postId: string): void => {
    setSendingRequest(true);
    axios.put<NoticeboardPostResponse, AxiosResponse<NoticeboardPostResponse>, NoticeboardPostRequest>(
      `/api/noticeboard/post/${postId}`, form.values)
      .then((response) => {
        setSendingRequest(false);
        props.onSubmit(response.data);
      })
      .catch(() => {
        setSendingRequest(false);
        setError(true);
      });
  };

  return (
    <>
      <form
        noValidate
        className={'noticeboard-post-form'}
        onSubmit={form.onSubmit(handleSubmit)}
      >
        {error && (
          <Alert icon={<IconAlertCircle size={16}/>} title={t('common.error')} color={'red'}>
            {props.variant === NoticeboardPostFormVariant.CREATE &&
              t('noticeboard.noticeboardPostForm.couldNotCreate')}
            {props.variant === NoticeboardPostFormVariant.UPDATE &&
              t('noticeboard.noticeboardPostForm.couldNotUpdate')}
          </Alert>
        )}
        <TextInput
          required
          label={t('noticeboard.noticeboardPostForm.title')}
          autoComplete={'noticeboard-post-title'}
          autoFocus={true}
          {...form.getInputProps('title')}
        />
        <Textarea
          required
          label={t('noticeboard.noticeboardPostForm.content')}
          autoComplete={'noticeboard-post-content'}
          autoFocus={true}
          autosize
          minRows={10}
          maxRows={10}
          {...form.getInputProps('content')}
        />
        <div className={'noticeboard-post-form-actions'}>
          {props.variant === NoticeboardPostFormVariant.UPDATE &&
            <Button
              leftIcon={<IconCheck size={18}/>}
              type={'submit'}
              loading={sendingRequest}
            >
              {t('noticeboard.noticeboardPostForm.updatePost')}
            </Button>}
          {props.variant === NoticeboardPostFormVariant.CREATE &&
            <Button
              leftIcon={<IconCheck size={18}/>}
              type={'submit'}
              loading={sendingRequest}
            >
              {t('noticeboard.noticeboardPostForm.createPost')}
            </Button>}
          <Button
            onClick={props.onCancel}
            variant={'default'}
          >
            {t('noticeboard.noticeboardPostDeleteConfirmation.cancel')}
          </Button>
        </div>
      </form>
    </>
  );
};