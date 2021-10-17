import * as dayjs from 'dayjs';

export interface IPayment {
  id?: number;
  name?: string;
  priceNet?: number;
  vat?: number;
  priceGross?: number | null;
  createTime?: dayjs.Dayjs | null;
  updateTime?: dayjs.Dayjs | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public name?: string,
    public priceNet?: number,
    public vat?: number,
    public priceGross?: number | null,
    public createTime?: dayjs.Dayjs | null,
    public updateTime?: dayjs.Dayjs | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
