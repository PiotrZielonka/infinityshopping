package infinityshopping.online.app.service.errors;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException() {
    super("A Product does not exist");
  }
}
