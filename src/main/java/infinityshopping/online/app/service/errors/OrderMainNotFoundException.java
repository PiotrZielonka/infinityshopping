package infinityshopping.online.app.service.errors;

public class OrderMainNotFoundException extends RuntimeException {

  public OrderMainNotFoundException() {
    super("A OrderMain does not exist");
  }
}
