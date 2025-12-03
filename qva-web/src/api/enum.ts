import axios from 'axios';

export function queryByType(type: string) {
  return axios.get(`/console/enums/${type}`);
}

export function updateOrder(orderId: number, data: any) {
  return axios.put<any>(`/console/orders/${orderId}`, data);
}

export function deleteOrder(orderId: number) {
  return axios.delete<any>(`/console/orders/${orderId}`);
}

export function queryRefundList() {
  return axios.get('/console/orders');
}
