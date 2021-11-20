import { IOrderMain } from 'app/entities/order-main/order-main.model';

export interface IPaymentOrderMain {
  id?: number;
  name?: string;
  priceNet?: number;
  vat?: number;
  priceGross?: number;
  orderMain?: IOrderMain | null;
}

export class PaymentOrderMain implements IPaymentOrderMain {
  constructor(
    public id?: number,
    public name?: string,
    public priceNet?: number,
    public vat?: number,
    public priceGross?: number,
    public orderMain?: IOrderMain | null
  ) {}
}

export function getPaymentOrderMainIdentifier(paymentOrderMain: IPaymentOrderMain): number | undefined {
  return paymentOrderMain.id;
}
