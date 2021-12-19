package infinityshopping.online.app.service.errors;

public class PaymentNotFoundException extends RuntimeException {

  public PaymentNotFoundException() {
    super("A Payment does not exist");
  }
}
