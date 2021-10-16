package infinityshopping.online.app.service;

import infinityshopping.online.app.service.dto.CartDtoAmountOfCartGross;
import infinityshopping.online.app.service.dto.CartDtoAmountsGross;
import java.util.Optional;

public interface CartService {

  Optional<CartDtoAmountsGross> findByUserIdAllAmountsGross();

  Optional<CartDtoAmountOfCartGross> findByUserIdAmountOfCartGross();
}
