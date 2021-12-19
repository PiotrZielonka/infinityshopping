package infinityshopping.online.app.service.errors;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User does not exist");
  }
}
