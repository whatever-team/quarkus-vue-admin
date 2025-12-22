declare module '@arco-design/web-vue/es/form' {
  export type FormInstance = {
    validate: (...args: any[]) => Promise<any>;
    validateField: (...args: any[]) => Promise<any>;
    resetFields: (...args: any[]) => void;
    clearValidate: (...args: any[]) => void;
    setFields: (data: Record<string, any>) => void;
  };
}

declare module '@arco-design/web-vue/es/form/interface' {
  export type ValidatedError = {
    label: string;
    field: string;
    value: any;
    type: string;
    isRequiredError: boolean;
    message: string;
  };
}
