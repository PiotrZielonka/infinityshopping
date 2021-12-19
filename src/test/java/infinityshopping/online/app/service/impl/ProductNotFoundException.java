package infinityshopping.online.app.service.impl;

public class ProductNotFoundException extends RuntimeException{

  public ProductNotFoundException() {
    super("A Product does not exist");
  }
}
