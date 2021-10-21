import { ICart } from 'app/entities/cart/cart.model';

export interface IShipmentCart {
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
  cart?: ICart | null;
}

export class ShipmentCart implements IShipmentCart {
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
    public cart?: ICart | null
  ) {}
}

export function getShipmentCartIdentifier(shipmentCart: IShipmentCart): number | undefined {
  return shipmentCart.id;
}
