package hbi.core.order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.core.order.dto.OmOrderHeaders;
import hbi.core.order.dto.OmOrderLines;

import java.util.List;

public interface OmOrderLinesMapper extends Mapper<OmOrderLines>{

    List<OmOrderLines> selectByHeaderId(OmOrderLines cuxSalesLines);
    List<OmOrderLines> selectSumPrice(OmOrderHeaders notice);
    boolean deleteByHeaderId(OmOrderLines cuxSalesLines);
}