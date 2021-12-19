package infinityshopping.online.app.service.errors;

public class ShipmentOrderMainNotFoundException extends RuntimeException {

  public ShipmentOrderMainNotFoundException() {
    super("A ShipmentOrderMain does not exist");
  }
}
