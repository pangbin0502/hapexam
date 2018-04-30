package hbi.core.order.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.core.order.dto.OmOrderLines;

import java.util.List;

public interface IOmOrderLinesService extends IBaseService<OmOrderLines>, ProxySelf<IOmOrderLinesService>{

    List<OmOrderLines> selectByHeaderId(IRequest requestContext, OmOrderLines cuxSalesLines, int page, int pagesize);
}