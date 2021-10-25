import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        data: { pageTitle: 'infinityshoppingApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'infinityshoppingApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'payment-cart',
        data: { pageTitle: 'infinityshoppingApp.paymentCart.home.title' },
        loadChildren: () => import('./payment-cart/payment-cart.module').then(m => m.PaymentCartModule),
      },
      {
        path: 'shipment-cart',
        data: { pageTitle: 'infinityshoppingApp.shipmentCart.home.title' },
        loadChildren: () => import('./shipment-cart/shipment-cart.module').then(m => m.ShipmentCartModule),
      },
      {
        path: 'product-in-cart',
        data: { pageTitle: 'infinityshoppingApp.productInCart.home.title' },
        loadChildren: () => import('./product-in-cart/product-in-cart.module').then(m => m.ProductInCartModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
