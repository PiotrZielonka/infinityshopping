package infinityshopping.online.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import infinityshopping.online.app.IntegrationTest;
import infinityshopping.online.app.config.Constants;
import infinityshopping.online.app.domain.OrderMain;
import infinityshopping.online.app.domain.ShipmentOrderMain;
import infinityshopping.online.app.domain.User;
import infinityshopping.online.app.domain.enumeration.OrderMainStatusEnum;
import infinityshopping.online.app.repository.OrderMainRepository;
import infinityshopping.online.app.repository.ShipmentOrderMainRepository;
import infinityshopping.online.app.security.AuthoritiesConstants;
import infinityshopping.online.app.service.AddVat;
import infinityshopping.online.app.service.dto.ShipmentOrderMainDTO;
import infinityshopping.online.app.service.mapper.ShipmentOrderMainMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
class ShipmentOrderMainResourceIT implements AddVat {

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
  private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_STREET = "AAAAAAAAAA";
  private static final String UPDATED_STREET = "BBBBBBBBBB";

  private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
  private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

  private static final String DEFAULT_CITY = "AAAAAAAAAA";
  private static final String UPDATED_CITY = "BBBBBBBBBB";

  private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

  private static final String DEFAULT_PHONE_TO_THE_RECEIVER = "AAAAAAAAAA";
  private static final String UPDATED_PHONE_TO_THE_RECEIVER = "BBBBBBBBBB";

  private static final String DEFAULT_FIRM = "AAAAAAAAAA";
  private static final String UPDATED_FIRM = "BBBBBBBBBB";

  private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
  private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

  // OrderMain
  private static final BigDecimal DEFAULT_VAT = new BigDecimal(random.nextInt(30 - 5) + 5);

  private static final String DEFAULT_BUYER_LOGIN = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_FIRST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_LAST_NAME = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_EMAIL = "AAAAAAAAAA";
  private static final String DEFAULT_BUYER_PHONE = "AAAAAAAAAA";
  private static final BigDecimal DEFAULT_AMOUNT_OF_CART_NET
      = new BigDecimal(random.nextInt(1000000));
  private final BigDecimal defaultAmountOfCartGross
      = addVat(DEFAULT_AMOUNT_OF_CART_NET, DEFAULT_VAT);

  private static final BigDecimal DEFAULT_AMOUNT_OF_SHIPMENT_NET
      = new BigDecimal(random.nextInt(100));

  private final BigDecimal defaultAmountOfShipmentGross
      = addVat(DEFAULT_AMOUNT_OF_SHIPMENT_NET, DEFAULT_VAT);

  private static final BigDecimal DEFAULT_AMOUNT_OF_ORDER_NET
      = DEFAULT_AMOUNT_OF_CART_NET.add(DEFAULT_AMOUNT_OF_SHIPMENT_NET);

  private final BigDecimal defaultAmountOfOrderGross
      = defaultAmountOfCartGross.add(defaultAmountOfShipmentGross);

  private static final OrderMainStatusEnum DEFAULT_ORDERMAIN_STATUS
      = OrderMainStatusEnum.PreparationForShipping;

  private static final Instant DEFAULT_CREATE_TIME_ORDER_MAIN = Instant.now()
      .truncatedTo(ChronoUnit.SECONDS);

  private static final Instant DEFAULT_UPDATE_TIME_ORDER_MAIN = Instant.now()
      .truncatedTo(ChronoUnit.SECONDS);

  private static final String ENTITY_API_URL = "/api/shipment-order-main";
  private static final String ENTITY_API_URL_ORDER_DETAILS
      = ENTITY_API_URL + "/orderDetails" + "/{id}";
  private static final String ENTITY_API_URL_EDIT = ENTITY_API_URL + "/edit" + "/{id}";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  @Autowired
  private ShipmentOrderMainRepository shipmentOrderMainRepository;

  @Autowired
  private OrderMainRepository orderMainRepository;

  @Autowired
  private ShipmentOrderMainMapper shipmentOrderMainMapper;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restShipmentOrderMainMockMvc;

  private OrderMain orderMain;

  private ShipmentOrderMain shipmentOrderMain;


  public static ShipmentOrderMain createEntity(EntityManager em) {
    ShipmentOrderMain shipmentOrderMain = new ShipmentOrderMain()
        .firstName(DEFAULT_FIRST_NAME)
        .lastName(DEFAULT_LAST_NAME)
        .street(DEFAULT_STREET)
        .postalCode(DEFAULT_POSTAL_CODE)
        .city(DEFAULT_CITY)
        .country(DEFAULT_COUNTRY)
        .phoneToTheReceiver(DEFAULT_PHONE_TO_THE_RECEIVER)
        .firm(DEFAULT_FIRM)
        .taxNumber(DEFAULT_TAX_NUMBER);
    return shipmentOrderMain;
  }

  public OrderMain createEntityOrderMain(EntityManager em) {
    OrderMain orderMain = new OrderMain()
        .buyerLogin(DEFAULT_BUYER_LOGIN)
        .buyerFirstName(DEFAULT_BUYER_FIRST_NAME)
        .buyerLastName(DEFAULT_BUYER_LAST_NAME)
        .buyerEmail(DEFAULT_BUYER_EMAIL)
        .buyerPhone(DEFAULT_BUYER_PHONE)
        .amountOfCartNet(DEFAULT_AMOUNT_OF_CART_NET)
        .amountOfCartGross(defaultAmountOfCartGross)
        .amountOfShipmentNet(DEFAULT_AMOUNT_OF_SHIPMENT_NET)
        .amountOfShipmentGross(defaultAmountOfShipmentGross)
        .amountOfOrderNet(DEFAULT_AMOUNT_OF_ORDER_NET)
        .amountOfOrderGross(defaultAmountOfOrderGross)
        .orderMainStatus(DEFAULT_ORDERMAIN_STATUS)
        .createTime(DEFAULT_CREATE_TIME_ORDER_MAIN)
        .updateTime(DEFAULT_UPDATE_TIME_ORDER_MAIN);
    return orderMain;
  }

  @BeforeEach
  public void initTest() {
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

    // given OrderMain
    orderMain = createEntityOrderMain(em);
    orderMainRepository.save(orderMain);

    // given ShipmentOrderMain
    shipmentOrderMain = createEntity(em);
    shipmentOrderMain.setOrderMain(orderMain);
    shipmentOrderMainRepository.save(shipmentOrderMain);

    // set relationship for OrderMain
    orderMain.setShipmentOrderMain(shipmentOrderMain);
    orderMainRepository.save(orderMain);
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getShipmentOrderMainByIdOrderMain() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, orderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(shipmentOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
        .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
        .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
        .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
        .andExpect(jsonPath("$.phoneToTheReceiver").value(DEFAULT_PHONE_TO_THE_RECEIVER))
        .andExpect(jsonPath("$.firm").value(DEFAULT_FIRM))
        .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER));
  }

  @Test
  @Transactional
  void getShipmentOrderMainByIdOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, orderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingShipmentOrderMainByIdOrderMain() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ORDER_DETAILS, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getShipmentOrderMain() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, shipmentOrderMain.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id").value(shipmentOrderMain.getId().intValue()))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
        .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
        .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
        .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
        .andExpect(jsonPath("$.phoneToTheReceiver").value(DEFAULT_PHONE_TO_THE_RECEIVER))
        .andExpect(jsonPath("$.firm").value(DEFAULT_FIRM))
        .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER));
  }

  @Test
  @Transactional
  void getShipmentOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, shipmentOrderMain.getId()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void getNonExistingShipmentOrderMain() throws Exception {
    // Get the shipmentOrderMain
    restShipmentOrderMainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkFirstNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setFirstName(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkLastNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setLastName(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkStreetIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setStreet(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkPostalCodeIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setPostalCode(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkCityIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setCity(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkCountryIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setCountry(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void checkPhoneToTheReceiverIsRequired() throws Exception {
    int databaseSizeBeforeTest = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setPhoneToTheReceiver(null);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNewShipmentOrderMain() throws Exception {
    final int databaseSizeBeforeUpdate = shipmentOrderMainRepository.findAll().size();

    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setFirstName(UPDATED_FIRST_NAME);
    shipmentOrderMainDto.setLastName(UPDATED_LAST_NAME);
    shipmentOrderMainDto.setStreet(UPDATED_STREET);
    shipmentOrderMainDto.setPostalCode(UPDATED_POSTAL_CODE);
    shipmentOrderMainDto.setCity(UPDATED_CITY);
    shipmentOrderMainDto.setCountry(UPDATED_COUNTRY);
    shipmentOrderMainDto.setPhoneToTheReceiver(UPDATED_PHONE_TO_THE_RECEIVER);
    shipmentOrderMainDto.setFirm(UPDATED_FIRM);
    shipmentOrderMainDto.setTaxNumber(UPDATED_TAX_NUMBER);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isOk());

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeUpdate);
    ShipmentOrderMain testShipmentOrderMain = shipmentOrderMainList.get(
        shipmentOrderMainList.size() - 1);
    assertThat(testShipmentOrderMain.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    assertThat(testShipmentOrderMain.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    assertThat(testShipmentOrderMain.getStreet()).isEqualTo(UPDATED_STREET);
    assertThat(testShipmentOrderMain.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    assertThat(testShipmentOrderMain.getCity()).isEqualTo(UPDATED_CITY);
    assertThat(testShipmentOrderMain.getCountry()).isEqualTo(UPDATED_COUNTRY);
    assertThat(testShipmentOrderMain.getPhoneToTheReceiver()).isEqualTo(
        UPDATED_PHONE_TO_THE_RECEIVER);
    assertThat(testShipmentOrderMain.getFirm()).isEqualTo(UPDATED_FIRM);
    assertThat(testShipmentOrderMain.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
    assertThat(testShipmentOrderMain.getOrderMain().getId()).isEqualTo(orderMain.getId());
  }

  @Test
  @Transactional
  @WithMockUser(username = "alice", authorities = AuthoritiesConstants.USER)
  void putNewShipmentOrderMainByUserShouldThrowStatusForbidden() throws Exception {
    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setFirstName(UPDATED_FIRST_NAME);
    shipmentOrderMainDto.setLastName(UPDATED_LAST_NAME);
    shipmentOrderMainDto.setStreet(UPDATED_STREET);
    shipmentOrderMainDto.setPostalCode(UPDATED_POSTAL_CODE);
    shipmentOrderMainDto.setCity(UPDATED_CITY);
    shipmentOrderMainDto.setCountry(UPDATED_COUNTRY);
    shipmentOrderMainDto.setPhoneToTheReceiver(UPDATED_PHONE_TO_THE_RECEIVER);
    shipmentOrderMainDto.setFirm(UPDATED_FIRM);
    shipmentOrderMainDto.setTaxNumber(UPDATED_TAX_NUMBER);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  void putNewShipmentOrderMainByAnyoneShouldThrowStatusUnauthorized() throws Exception {
    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setFirstName(UPDATED_FIRST_NAME);
    shipmentOrderMainDto.setLastName(UPDATED_LAST_NAME);
    shipmentOrderMainDto.setStreet(UPDATED_STREET);
    shipmentOrderMainDto.setPostalCode(UPDATED_POSTAL_CODE);
    shipmentOrderMainDto.setCity(UPDATED_CITY);
    shipmentOrderMainDto.setCountry(UPDATED_COUNTRY);
    shipmentOrderMainDto.setPhoneToTheReceiver(UPDATED_PHONE_TO_THE_RECEIVER);
    shipmentOrderMainDto.setFirm(UPDATED_FIRM);
    shipmentOrderMainDto.setTaxNumber(UPDATED_TAX_NUMBER);

    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putNonExistingShipmentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = shipmentOrderMainRepository.findAll().size();
    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain
        = shipmentOrderMainRepository.findById(shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL_EDIT, shipmentOrderMainDto.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithIdMismatchShipmentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = shipmentOrderMainRepository.findAll().size();
    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain = shipmentOrderMainRepository.findById(
        shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restShipmentOrderMainMockMvc
        .perform(put(ENTITY_API_URL_EDIT, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isBadRequest());

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  @WithMockUser(username = "admin", password = "admin", authorities = AuthoritiesConstants.ADMIN)
  void putWithMissingIdPathParamShipmentOrderMain() throws Exception {
    int databaseSizeBeforeUpdate = shipmentOrderMainRepository.findAll().size();
    // Update the shipmentOrderMain
    ShipmentOrderMain updatedShipmentOrderMain = shipmentOrderMainRepository.findById(
        shipmentOrderMain.getId()).get();

    ShipmentOrderMainDTO shipmentOrderMainDto
        = shipmentOrderMainMapper.toDto(updatedShipmentOrderMain);
    shipmentOrderMainDto.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restShipmentOrderMainMockMvc.perform(put(ENTITY_API_URL + "/edit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shipmentOrderMainDto)))
        .andExpect(status().isMethodNotAllowed());

    // Validate the ShipmentOrderMain in the database
    List<ShipmentOrderMain> shipmentOrderMainList = shipmentOrderMainRepository.findAll();
    assertThat(shipmentOrderMainList).hasSize(databaseSizeBeforeUpdate);
  }
}
