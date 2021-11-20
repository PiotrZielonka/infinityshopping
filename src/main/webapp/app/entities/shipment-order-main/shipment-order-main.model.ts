import { IOrderMain } from 'app/entities/order-main/order-main.model';

export interface IShipmentOrderMain {
  id?: number;
  firstName?: string;
  lastName?: string;
  street?: string;
  postalCode?: string;
  city?: string;
  country?: string;
  phoneToTheReceiver?: string;
  firm?: string | null;
  taxNumber?: string | null;
  orderMain?: IOrderMain | null;
}

export class ShipmentOrderMain implements IShipmentOrderMain {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public street?: string,
    public postalCode?: string,
    public city?: string,
    public country?: string,
    public phoneToTheReceiver?: string,
    public firm?: string | null,
    public taxNumber?: string | null,
    public orderMain?: IOrderMain | null
  ) {}
}

export function getShipmentOrderMainIdentifier(shipmentOrderMain: IShipmentOrderMain): number | undefined {
  return shipmentOrderMain.id;
}
