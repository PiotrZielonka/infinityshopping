package infinityshopping.online.app.service;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User does not exist");
  }
}
