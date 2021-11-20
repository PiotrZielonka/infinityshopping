package infinityshopping.online.app.service.mapper;

import infinityshopping.online.app.domain.ProductInOrderMain;
import infinityshopping.online.app.service.dto.ProductInOrderMainDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMainMapper.class})
public interface ProductInOrderMainMapper extends
    EntityMapper<ProductInOrderMainDTO, ProductInOrderMain> {

  @Mapping(target = "orderMain", source = "orderMain", qualifiedByName = "id")
  ProductInOrderMainDTO toDto(ProductInOrderMain s);
}
