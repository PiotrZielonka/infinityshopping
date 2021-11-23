package infinityshopping.online.app.web.rest;

import static infinityshopping.online.app.web.rest.TestUtil.sameNumber;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.Cart;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.repository.CartRepository;
import infinityshopping.online.app.repository.UserRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.security.SecurityUtils;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.UserNotFoundException;
import infinityshopping.online.app.service.mapper.CartMapper;
import java.math.BigDecimal;
import java.util.Random;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
class CartResourceIT implements AddVat {

  private static Random random = new Random();

  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET
      = new BigDecimal(random.nextInt(10000));
  private final BigDecimal defaultAmountOfCartGross
      = addVat(DEFAULT_AMOUNT_OF_CART_NET, new BigDecimal(random.nextInt(30 - 5) + 5));

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET
      = new BigDecimal(random.nextInt(100));
  private final BigDecimal defaultAmountOfShipmentGross
      = addVat(DEFAULT_AMOUNT_OF_SHIPMENT_NET, new BigDecimal(random.nextInt(30 - 5) + 5));

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET
      = DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
  private final BigDecimal defaultAmountOfOrderGross
      = defaultAmountOfCartGross.add(defaultAmountOfShipmentGross);

  private static final String ENTITY_API_URL = "/api/cart/userCart";
  private static final String ENTITY_API_URL_AMOUNTS_GROSS = ENTITY_API_URL + "/amountsGross";
  private static final String ENTITY_API_URL_AMOUNT_OF_CART_GROSS
      = ENTITY_API_URL + "/amountOfCartGross";

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CartMapper cartMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restCartMockMvc;

  private Cart cart;

  private User currentLoggedUser;

  @BeforeEach
  public void initUsers() throws Exception {
    // given
    // given User
    User user = new User();
    user.setLogin("alice");
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setEmail("alice@example.com");
    user.setFirstName("Alice");
    user.setLastName("Something");
    user.setImageUrl("http://placehold.it/50x50");
    user.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Cart for User
    Cart cart = new Cart();
    cart.setUser(user);
    cart.setAmountOfCartNet(DEFAULT_AMOUNT_OF_CART_NET);
    cart.setAmountOfCartGross(defaultAmountOfCartGross);
    cart.setAmountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET);
    cart.setAmountOfShipmentGross(defaultAmountOfShipmentGross);
    cart.setAmountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET);
    cart.setAmountOfOrderGross(defaultAmountOfOrderGross);
    cartRepository.save(cart);
    user.setCart(cart);
    userRepository.saveAndFlush(user);

    // given User2
    User user2 = new User();
    user2.setLogin("caroline");
    user2.setPassword(RandomStringUtils.random(60));
    user2.setActivated(true);
    user2.setEmail("caroline@example.com");
    user2.setFirstName("Caroline");
    user2.setLastName("Something");
    user2.setImageUrl("http://placehold.it/50x50");
    user2.setLangKey(Constants.DEFAULT_LANGUAGE);

    // given Cart2 for User2
    Cart cart2 = new Cart();
    cart2.setUser(user2);
    cart2.setAmountOfCartNet(BigDecimal.ZERO);
    cart2.setAmountOfCartGross(BigDecimal.ZERO);
    cart2.setAmountOfShipmentNet(BigDecimal.ZERO);
    cart2.setAmountOfShipmentGross(BigDecimal.ZERO);
    cart2.setAmountOfOrderNet(BigDecimal.ZERO);
    cart2.setAmountOfOrderGross(BigDecimal.ZERO);
    cartRepository.save(cart2);
    user2.setCart(cart2);
    userRepository.saveAndFlush(user2);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAllAmountsGrossOfCurrentLoggedUser() throws Exception {
    currentLoggedUser = checkIfUserExist();

    // Get all the cartList
    restCartMockMvc.perform(get(ENTITY_API_URL_AMOUNTS_GROSS))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(currentLoggedUser.getCart().getId().intValue()))
        .andExpect(jsonPath("$.amountOfCartNet").doesNotExist())
        .andExpect(jsonPath("$.amountOfCartGross").value(sameNumber(defaultAmountOfCartGross)))
        .andExpect(jsonPath("$.amountOfShipmentNet").doesNotExist())
        .andExpect(
            jsonPath("$.amountOfShipmentGross").value(sameNumber(defaultAmountOfShipmentGross)))
        .andExpect(jsonPath("$.amountOfOrderNet").doesNotExist())
        .andExpect(
            jsonPath("$.amountOfOrderGross").value(sameNumber(defaultAmountOfOrderGross)));
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getAmountOfCartGrossOfCurrentLoggedUser() throws Exception {
    currentLoggedUser = checkIfUserExist();

    // Get all the cartList
    restCartMockMvc.perform(get(ENTITY_API_URL_AMOUNT_OF_CART_GROSS))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(currentLoggedUser.getCart().getId().intValue()))
        .andExpect(jsonPath("$.amountOfCartNet").doesNotExist())
        .andExpect(jsonPath("$.amountOfCartGross").value(sameNumber(defaultAmountOfCartGross)))
        .andExpect(jsonPath("$.amountOfShipmentNet").doesNotExist())
        .andExpect(jsonPath("$.amountOfShipmentGross").doesNotExist())
        .andExpect(jsonPath("$.amountOfOrderNet").doesNotExist())
        .andExpect(jsonPath("$.amountOfOrderGross").doesNotExist());
  }

  private User checkIfUserExist() {
    currentLoggedUser = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new UserNotFoundException()))
        .orElseThrow(() -> new UserNotFoundException());
    return currentLoggedUser;
  }
}
