import { IUser } from 'app/entities/user/user.model';

export interface ICart {
  id?: number;
  amountOfCartNet?: number;
  amountOfCartGross?: number;
  amountOfShipmentNet?: number;
  amountOfShipmentGross?: number;
  amountOfOrderNet?: number;
  amountOfOrderGross?: number;
  user?: IUser | null;
}

export class Cart implements ICart {
  constructor(
    public id?: number,
    public amountOfCartNet?: number,
    public amountOfCartGross?: number,
    public amountOfShipmentNet?: number,
    public amountOfShipmentGross?: number,
    public amountOfOrderNet?: number,
    public amountOfOrderGross?: number,
    public user?: IUser | null
  ) {}
}

export function getCartIdentifier(cart: ICart): number | undefined {
  return cart.id;
}
