import { Pipe, PipeTransform } from '@angular/core';
import { IOrderMain } from 'app/entities/order-main/order-main.model';

@Pipe({
  name: 'searchOrderMain'
})
export class SearchOrderMainPipe implements PipeTransform {
  transform(orderMains: IOrderMain[], text: any): IOrderMain[] {
    if (text == null || text === '') {
      return orderMains;
    }
    return orderMains.filter(
      oM =>
        oM.buyerLogin!.includes(text) ||
        oM.buyerFirstName!.includes(text) ||
        oM.buyerLastName!.includes(text) ||
        oM.buyerEmail!.includes(text) ||
        oM.id!.toString().includes(text) 
    );
  }
}
