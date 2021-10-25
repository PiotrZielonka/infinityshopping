package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.ProductInCart;
import infinityshopping.online.app.service.dto.ProductInCartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartMapper.class})
public interface ProductInCartMapper extends EntityMapper<ProductInCartDTO, ProductInCart> {

  @Mapping(target = "cart", source = "cart", qualifiedByName = "id")
  ProductInCartDTO toDto(ProductInCart s);
}
