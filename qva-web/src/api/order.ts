import axios from 'axios';

export function queryOrderList(params: any) {
  return axios.get('/console/ok/orders', { params });
}

export function updateOrder(orderId: number, data: any) {
  return axios.put<any>(`/console/ok/orders/${orderId}`, data);
}

export function deleteOrder(orderId: string) {
  return axios.delete<any>(`/console/ok/orders/${orderId}`);
}

export function queryRefundList(params: any) {
  return axios.get('/console/ok/refunds', { params });
}

export function deleteRefund(orderId: string) {
  return axios.delete<any>(`/console/ok/refunds/${orderId}`);
}
