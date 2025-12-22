export interface AnyObject {
  [key: string]: unknown;
}

export interface Options {
  value: unknown;
  label: string;
}

export interface NodeOptions extends Options {
  children?: NodeOptions[];
}

export interface HttpResponse<T = unknown> {
  serial: string;
  message: string;
  code: string;
  result: T;
  exception: string;
}

export interface GetParams {
  body: null;
  type: string;
  url: string;
}

export interface PostData {
  body: string;
  type: string;
  url: string;
}

export interface Pagination {
  current: number;
  pageSize: number;
  total?: number;
  sortField?: string;
  sortType?: 'Descending' | 'Ascending';
  showTotal?: boolean;
  showMore?: boolean;
  showPageSize?: boolean;
  showJumper?: boolean;
}

export type TimeRanger = [string, string];

export interface GeneralChart {
  xAxis: string[];
  data: Array<{ name: string; value: number[] }>;
}

export interface Enum {
  code: string;
  desc: string;
}

export type SortType = NonNullable<Pagination['sortType']>;
