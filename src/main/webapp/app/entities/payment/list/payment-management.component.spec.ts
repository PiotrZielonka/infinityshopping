import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { PaymentService } from '../service/payment.service';
import { PaymentManagementComponent } from './payment-management.component';

describe('Component Tests', () => {
  describe('Payment Management Component', () => {
    let comp: PaymentManagementComponent;
    let fixture: ComponentFixture<PaymentManagementComponent>;
    let service: PaymentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentManagementComponent],
      })
        .overrideTemplate(PaymentManagementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentManagementComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PaymentService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.payments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
