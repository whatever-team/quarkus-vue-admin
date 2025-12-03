import axios from 'axios';

export function fetchQuery(params: any) {
  console.log('fetch query', params);
  return axios.get('/sys-user', { params });
}

export function fetchCreate(data: any) {
  return axios.post<any>(`/sys-user`, data);
}

export function fetchUpdate(data: any) {
  return axios.put<any>(`/sys-user`, data);
}

export function fetchDelete(id: string) {
  return axios.delete<any>(`/sys-user/${id}`);
}
