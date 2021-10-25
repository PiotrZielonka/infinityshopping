package infinityshopping.online.app.service;

import java.math.BigDecimal;

public interface AddVat {

  default BigDecimal addVat(BigDecimal priceNet, BigDecimal vat) {
    return priceNet.add(priceNet.multiply(vat.movePointLeft(2)));
  }

}
