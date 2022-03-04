import { createAsyncThunk, createSlice, Draft } from '@reduxjs/toolkit';
import { UserResponse } from '../../dto/user';
import axios, { AxiosResponse } from 'axios';

interface CurrentUserState {
  user: UserResponse | null;
  isAuthenticated: boolean;
  isFetching: boolean;
}

export const TOKEN_KEY = 'EPISTIMI_TOKEN';

export const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: null,
    isAuthenticated: false,
    isFetching: false,
  } as CurrentUserState,
  reducers: {
    removeCurrentUser: (state: Draft<CurrentUserState>) => {
      state.user = null;
      state.isAuthenticated = false;
      state.isFetching = false;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchCurrentUser.pending, (state: Draft<CurrentUserState>) => {
        state.isAuthenticated = false;
        state.isFetching = true;
        state.user = null;
      })
      .addCase(fetchCurrentUser.fulfilled, (state: Draft<CurrentUserState>, action) => {
        state.isAuthenticated = true;
        state.isFetching = false;
        state.user = action.payload;
      })
      .addCase(fetchCurrentUser.rejected, (state: Draft<CurrentUserState>) => {
        state.isAuthenticated = false;
        state.isFetching = false;
        state.user = null;
      });
  },
});

export const fetchCurrentUser = createAsyncThunk(
  'auth/login',
  async (): Promise<UserResponse> => {
    if (!localStorage.getItem(TOKEN_KEY)) {
      return Promise.reject('not authenticated');
    }
    return axios.get<UserResponse>('api/user/current')
      .then((response: AxiosResponse<UserResponse>) => {
        return response.data;
      })
      .catch((e) => {
        return Promise.reject(e.response.data);
      });
  },
);

export const { removeCurrentUser } = authSlice.actions;
export const currentUserReducer = authSlice.reducer;
