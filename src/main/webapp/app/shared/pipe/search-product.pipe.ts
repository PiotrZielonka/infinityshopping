import { Pipe, PipeTransform } from '@angular/core';
import { IProduct } from 'app/entities/product/product.model';

@Pipe({
  name: 'searchProduct',
})
export class SearchProductPipe implements PipeTransform {
  transform(products: IProduct[], text: any): IProduct[] {
    if (text == null || text === '') {
      return products;
    }
    return products.filter(
      p => 
      p.name!.includes(text) ||
      p.id!.toString().includes(text)
    );
  }
}
