import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { PaymentCartService } from '../service/payment-cart.service';
import { PaymentCartComponent } from './payment-cart.component';
import { PaymentCart } from '../payment-cart.model';

describe('Component Tests', () => {
  describe('PaymentCart Management Component', () => {
    let comp: PaymentCartComponent;
    let fixture: ComponentFixture<PaymentCartComponent>;
    let service: PaymentCartService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentCartComponent],
      })
        .overrideTemplate(PaymentCartComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentCartComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PaymentCartService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'queryPaymentCartOfCurrentLoggedUser').mockReturnValue(
        of(
          new HttpResponse({
            body: (new PaymentCart(123)),
            headers
          })
        )
      );
    });

    it('Should call load a paymentCart of currentLoggedUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.queryPaymentCartOfCurrentLoggedUser).toHaveBeenCalled();
      expect(comp.paymentCart).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
