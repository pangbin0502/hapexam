package hbi.core.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import hbi.core.order.mapper.OmOrderLinesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hbi.core.order.dto.OmOrderLines;
import hbi.core.order.service.IOmOrderLinesService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmOrderLinesServiceImpl extends BaseServiceImpl<OmOrderLines> implements IOmOrderLinesService{
    @Autowired
    private OmOrderLinesMapper cuxSalesLinesMapper;

    public List<OmOrderLines> selectByHeaderId(IRequest requestContext, OmOrderLines cuxSalesLines, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return cuxSalesLinesMapper.selectByHeaderId(cuxSalesLines);

    }
}