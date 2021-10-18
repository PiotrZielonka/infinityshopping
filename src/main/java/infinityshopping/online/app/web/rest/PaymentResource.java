package infinityshopping.online.app.web.rest;

import infinityshopping.online.app.repository.PaymentRepository;
import infinityshopping.online.app.service.PaymentService;
import infinityshopping.online.app.service.dto.PaymentDTO;
import infinityshopping.online.app.service.dto.PaymentDtoNamePriceGross;
import infinityshopping.online.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class PaymentResource {

  private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

  private static final String ENTITY_NAME = "payment";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PaymentService paymentService;

  private final PaymentRepository paymentRepository;

  public PaymentResource(PaymentService paymentService, PaymentRepository paymentRepository) {
    this.paymentService = paymentService;
    this.paymentRepository = paymentRepository;
  }

  @PostMapping("/payments")
  public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO paymentDto)
      throws URISyntaxException {
    log.debug("REST request to save Payment : {}", paymentDto);
    if (paymentDto.getId() != null) {
      throw new BadRequestAlertException("A new payment cannot already have an ID", ENTITY_NAME,
          "idexists");
    }
    PaymentDTO result = paymentService.save(paymentDto);
    return ResponseEntity
        .created(new URI("/api/payments/" + result.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME,
            result.getId().toString()))
        .body(result);
  }

  @PutMapping("/payments/{id}")
  public ResponseEntity<PaymentDTO> updatePayment(
      @PathVariable(value = "id", required = false) final Long id,
      @Valid @RequestBody PaymentDTO paymentDto
  ) throws URISyntaxException {
    log.debug("REST request to update Payment : {}, {}", id, paymentDto);
    if (paymentDto.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, paymentDto.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!paymentRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    PaymentDTO result = paymentService.save(paymentDto);
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
            paymentDto.getId().toString()))
        .body(result);
  }

  @GetMapping("/payments")
  public List<PaymentDTO> getAllPayments() {
    log.debug("REST request to get all Payments");
    return paymentService.findAll();
  }

  @GetMapping("/payments/namePriceGross")
  public List<PaymentDtoNamePriceGross> getAllPaymentsOnlyWithNamePriceGross() {
    log.debug("REST request to get all Payments only with name and price gross");
    return paymentService.findAllNamePriceGross();
  }

  @GetMapping("/payments/{id}")
  public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
    log.debug("REST request to get Payment : {}", id);
    Optional<PaymentDTO> paymentDto = paymentService.findOne(id);
    return ResponseUtil.wrapOrNotFound(paymentDto);
  }

  @DeleteMapping("/payments/{id}")
  public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
    log.debug("REST request to delete Payment : {}", id);
    paymentService.delete(id);
    return ResponseEntity
        .noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
