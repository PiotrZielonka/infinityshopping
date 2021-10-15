package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.service.CartService;
import infinityshopping.online.app.service.dto.CartDtoAmountOfCartGross;
import infinityshopping.online.app.service.dto.CartDtoAmountsGross;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private static final String ENTITY_NAME = "cart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartService cartService;

    public CartResource(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/userCart/amountsGross")
    public ResponseEntity<CartDtoAmountsGross> getAllAmountsGrossOfCurrentLoggedUser() {
        log.debug("REST request to get all the amounts gross of a current logged user");
        Optional<CartDtoAmountsGross> cartDtoAmountsGross = cartService.findByUserIdAllAmountsGross();
        return ResponseUtil.wrapOrNotFound(cartDtoAmountsGross);
    }

    @GetMapping("/cart/userCart/amountOfCartGross")
    public ResponseEntity<CartDtoAmountOfCartGross> getAmountOfCartGrossOfCurrentLoggedUser() {
        log.debug("REST request to get the amount of cart gross of a current logged user");
        Optional<CartDtoAmountOfCartGross> cartDtoAmountCartGross = cartService.findByUserIdAmountOfCartGross();
        return ResponseUtil.wrapOrNotFound(cartDtoAmountCartGross);
    }
}
