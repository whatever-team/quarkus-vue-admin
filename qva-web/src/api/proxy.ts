import axios from 'axios';
import qs from 'query-string';

export function pay(partner: string, params: any) {
  return axios.get(`/v2/pay/${partner}`, { params });
}

export function directPayment(url: string, data: any) {
  const instance = axios.create();
  return instance.post(url, data, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
    },
  });
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
