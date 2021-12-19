package infinityshopping.online.app.service.errors;

public class CartNotFoundException extends RuntimeException {

  public CartNotFoundException() {
    super("A Cart does not exist");
  }
}
