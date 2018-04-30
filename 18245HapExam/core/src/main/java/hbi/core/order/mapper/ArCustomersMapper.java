package hbi.core.order.mapper;

import com.hand.hap.mybatis.common.Mapper;
import hbi.core.order.dto.ArCustomers;

public interface ArCustomersMapper extends Mapper<ArCustomers>{

    ArCustomers selectCustomerName(Long customerId);
}