import axios from 'axios';
import type { RouteRecordNormalized } from 'vue-router';
import { UserState } from '@/store/modules/user/types';
import type { HttpResponse } from '@/types/global';

export interface LoginData {
  username: string;
  password: string;
}

export interface LoginRes extends HttpResponse {
  result: {
    data: {
      user: any;
      expireAt: any;
      permissions: any[];
      token: string;
    };
  };
}

export function login(data: LoginData) {
  return axios.post<LoginRes>('/auth/login', data);
}

export function logout() {
  return axios.post<LoginRes>('/console/logout');
}

export function getUserInfo() {
  return axios.get<UserState>('/auth/info');
}

export function getMenuList() {
  return axios.post<RouteRecordNormalized[]>('/api/user/menu');
}
