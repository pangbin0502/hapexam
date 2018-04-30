package hbi.core.order.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
import hbi.core.order.dto.OmOrderHeaders;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IOmOrderHeadersService extends IBaseService<OmOrderHeaders>, ProxySelf<IOmOrderHeadersService>{
    void printPDF(Long headerId,HttpServletResponse response);
    void deleteById(List<OmOrderHeaders> omOrderHeaders);
    void exportExcel(IRequest request,OmOrderHeaders omOrderHeaders,int page,int pageSize,HttpServletResponse response);
    List<OmOrderHeaders> select2(IRequest request,OmOrderHeaders omOrderHeaders,int page,int pageSize);
}