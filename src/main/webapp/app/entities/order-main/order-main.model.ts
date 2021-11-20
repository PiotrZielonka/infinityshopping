import * as dayjs from 'dayjs';
import { IProductInOrderMain } from 'app/entities/product-in-order-main/product-in-order-main.model';
import { OrderMainStatusEnum } from 'app/entities/enumerations/order-main-status-enum.model';

export interface IOrderMain {
  id?: number;
  buyerLogin?: string | null;
  buyerFirstName?: string | null;
  buyerLastName?: string | null;
  buyerEmail?: string | null;
  buyerPhone?: string | null;
  amountOfCartNet?: number | null;
  amountOfCartGross?: number | null;
  amountOfShipmentNet?: number | null;
  amountOfShipmentGross?: number | null;
  amountOfOrderNet?: number | null;
  amountOfOrderGross?: number | null;
  orderMainStatus?: OrderMainStatusEnum | null;
  createTime?: dayjs.Dayjs | null;
  updateTime?: dayjs.Dayjs | null;
  productInOrderMains?: IProductInOrderMain[] | null;
}

export class OrderMain implements IOrderMain {
  constructor(
    public id?: number,
    public buyerLogin?: string | null,
    public buyerFirstName?: string | null,
    public buyerLastName?: string | null,
    public buyerEmail?: string | null,
    public buyerPhone?: string | null,
    public amountOfCartNet?: number | null,
    public amountOfCartGross?: number | null,
    public amountOfShipmentNet?: number | null,
    public amountOfShipmentGross?: number | null,
    public amountOfOrderNet?: number | null,
    public amountOfOrderGross?: number | null,
    public orderMainStatus?: OrderMainStatusEnum | null,
    public createTime?: dayjs.Dayjs | null,
    public updateTime?: dayjs.Dayjs | null,
    public productInOrderMains?: IProductInOrderMain[] | null
  ) {}
}

export function getOrderMainIdentifier(orderMain: IOrderMain): number | undefined {
  return orderMain.id;
}
