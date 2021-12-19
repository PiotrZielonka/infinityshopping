package infinityshopping.online.app.service.errors;

public class ProductInCartNotFoundException extends RuntimeException {

  public ProductInCartNotFoundException() {
    super("A ProductInCart does not exist");
  }
}
