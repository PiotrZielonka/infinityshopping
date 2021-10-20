import { ICart } from 'app/entities/cart/cart.model';

export interface IPaymentCart {
  id?: number;
  name?: string;
  priceNet?: number;
  vat?: number;
  priceGross?: number;
  cart?: ICart | null;
}

export class PaymentCart implements IPaymentCart {
  constructor(
    public id?: number,
    public name?: string,
    public priceNet?: number,
    public vat?: number,
    public priceGross?: number,
    public cart?: ICart | null
  ) {}
}

export function getPaymentCartIdentifier(paymentCart: IPaymentCart): number | undefined {
  return paymentCart.id;
}
