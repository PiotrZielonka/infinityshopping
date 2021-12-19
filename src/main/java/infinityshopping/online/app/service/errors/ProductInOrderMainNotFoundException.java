package infinityshopping.online.app.service.errors;

public class ProductInOrderMainNotFoundException extends RuntimeException {

  public ProductInOrderMainNotFoundException() {
    super("A ProductInOrderMain does not exist");
  }
}
