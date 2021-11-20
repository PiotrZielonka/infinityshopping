import { IOrderMain } from 'app/entities/order-main/order-main.model';

export interface IProductInOrderMain {
  id?: number;
  category?: string | null;
  name?: string | null;
  quantity?: number | null;
  priceNet?: number | null;
  vat?: number | null;
  priceGross?: number | null;
  totalPriceNet?: number | null;
  totalPriceGross?: number | null;
  stock?: number | null;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  productId?: number | null;
  orderMain?: IOrderMain | null;
}

export class ProductInOrderMain implements IProductInOrderMain {
  constructor(
    public id?: number,
    public category?: string | null,
    public name?: string | null,
    public quantity?: number | null,
    public priceNet?: number | null,
    public vat?: number | null,
    public priceGross?: number | null,
    public totalPriceNet?: number | null,
    public totalPriceGross?: number | null,
    public stock?: number | null,
    public description?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public productId?: number | null,
    public orderMain?: IOrderMain | null
  ) {}
}

export function getProductInOrderMainIdentifier(productInOrderMain: IProductInOrderMain): number | undefined {
  return productInOrderMain.id;
}
