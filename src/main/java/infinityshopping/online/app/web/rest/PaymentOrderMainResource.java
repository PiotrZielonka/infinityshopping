package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.repository.PaymentOrderMainRepository;
import infinityshopping.online.app.service.PaymentOrderMainService;
import infinityshopping.online.app.service.dto.PaymentOrderMainDTO;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class PaymentOrderMainResource {

  private final Logger log = LoggerFactory.getLogger(PaymentOrderMainResource.class);

  private static final String ENTITY_NAME = "paymentOrderMain";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PaymentOrderMainService paymentOrderMainService;

  private final PaymentOrderMainRepository paymentOrderMainRepository;

  public PaymentOrderMainResource(
      PaymentOrderMainService paymentOrderMainService,
      PaymentOrderMainRepository paymentOrderMainRepository
  ) {
    this.paymentOrderMainService = paymentOrderMainService;
    this.paymentOrderMainRepository = paymentOrderMainRepository;
  }

  @PutMapping("/payment-order-main/edit/{id}")
  public ResponseEntity<PaymentOrderMainDTO> updatePaymentOrderMain(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody PaymentOrderMainDTO paymentOrderMainDto
  ) throws URISyntaxException {
    log.debug("REST request to update PaymentOrderMain : {}, {}", id, paymentOrderMainDto);
    if (paymentOrderMainDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, paymentOrderMainDto.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!paymentOrderMainRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    PaymentOrderMainDTO result = paymentOrderMainService.save(paymentOrderMainDto);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(
            applicationName, true, ENTITY_NAME, paymentOrderMainDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/payment-order-main/orderDetails/{id}")
  public ResponseEntity<PaymentOrderMainDTO> getPaymentOrderMainByIdOrderMain(
      @PathVariable Long id) {
    log.debug("REST request to get PaymentOrderMain by id OrderMain");
    Optional<PaymentOrderMainDTO> paymentOrderMainDto
        = paymentOrderMainService.findByOrderMainId(id);
    return ResponseUtil.wrapOrNotFound(paymentOrderMainDto);
  }

  @GetMapping("/payment-order-main/{id}")
  public ResponseEntity<PaymentOrderMainDTO> getPaymentOrderMain(@PathVariable Long id) {
    log.debug("REST request to get PaymentOrderMain : {}", id);
    Optional<PaymentOrderMainDTO> paymentOrderMainDto = paymentOrderMainService.findOne(id);
    return ResponseUtil.wrapOrNotFound(paymentOrderMainDto);
  }
}
