import axios from 'axios';

export function queryProviderList(params: any) {
  return axios.get('/console/ok/providers', { params });
}

export function updateOrder(orderId: number, data: any) {
  return axios.put<any>(`/console/orders/${orderId}`, data);
}

export function deleteOrder(orderId: number) {
  return axios.delete<any>(`/console/orders/${orderId}`);
}
