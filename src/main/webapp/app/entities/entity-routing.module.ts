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
      {
        path: 'order-main',
        data: { pageTitle: 'infinityshoppingApp.orderMain.home.title' },
        loadChildren: () => import('./order-main/order-main.module').then(m => m.OrderMainModule),
      },
      {
        path: 'payment-order-main',
        data: { pageTitle: 'infinityshoppingApp.paymentOrderMain.home.title' },
        loadChildren: () => import('./payment-order-main/payment-order-main.module').then(m => m.PaymentOrderMainModule),
      },
      {
        path: 'product-in-order-main',
        data: { pageTitle: 'infinityshoppingApp.productInOrderMain.home.title' },
        loadChildren: () => import('./product-in-order-main/product-in-order-main.module').then(m => m.ProductInOrderMainModule),
      },
      {
        path: 'shipment-order-main',
        data: { pageTitle: 'infinityshoppingApp.shipmentOrderMain.home.title' },
        loadChildren: () => import('./shipment-order-main/shipment-order-main.module').then(m => m.ShipmentOrderMainModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
