package hbi.core.order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.core.order.dto.OrgCompanys;

public interface OrgCompanysMapper extends Mapper<OrgCompanys>{
    OrgCompanys selectCompanyName(Long companyId);
}