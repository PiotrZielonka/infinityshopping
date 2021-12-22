package infinityshopping.online.app.service.errors;

public class PaymentOrderMainNotFoundException extends RuntimeException {

  public PaymentOrderMainNotFoundException() {
    super("A PaymentOrderMain does not exist");
  }
}
