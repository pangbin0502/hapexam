package hbi.core.order.controllers;

import com.hand.hap.excel.annotation.ExcelExport;
import hbi.core.order.dto.OmOrderLines;
import hbi.core.order.mapper.OmOrderLinesMapper;
import hbi.core.order.service.IOmOrderLinesService;
import org.springframework.stereotype.Controller;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.ResponseData;
import hbi.core.order.dto.OmOrderHeaders;
import hbi.core.order.service.IOmOrderHeadersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
    public class OmOrderHeadersController extends BaseController {

    @Autowired
    private IOmOrderHeadersService service;
    @Autowired
    private IOmOrderLinesService lservice;
    @Autowired
    private OmOrderLinesMapper omOrderLinesMapper;

    private List<OmOrderHeaders> list;

    public List<OmOrderHeaders> getList() {
        return list;
    }

    public void setList(List<OmOrderHeaders> list) {
        this.list = list;
    }


    @RequestMapping(value = "/hap/om/order/headers/submit")
    @ResponseBody
    public ResponseData update(@RequestBody List<OmOrderHeaders> dto, BindingResult result, HttpServletRequest request) {
        getValidator().validate(dto, result);
        if (result.hasErrors()) {
            ResponseData responseData = new ResponseData(false);
            responseData.setMessage(getErrorMessage(result, request));
            return responseData;
        }
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/hap/om/order/headers/submitall")
    @ResponseBody
    public ResponseData updateAll(@RequestBody List<OmOrderHeaders> dto, BindingResult result, HttpServletRequest request) {
        IRequest requestCtx = createRequestContext(request);
        for (OmOrderHeaders omOrderHeaders : dto) {
            if (omOrderHeaders.getHeaderId() == null) {
                service.insertSelective(requestCtx, omOrderHeaders);
                if (omOrderHeaders.getOmOrderLiness() != null) {
                    processOmOrderLines(omOrderHeaders);
                }
            } else {
                service.updateByPrimaryKeySelective(requestCtx, omOrderHeaders);
                if (omOrderHeaders.getOmOrderLiness() != null) {
                    processOmOrderLines(omOrderHeaders);
                }
            }
        }
        return new ResponseData(dto);
    }

    @RequestMapping(value = "/hap/om/order/headers/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<OmOrderHeaders> dto) {
        service.deleteById(dto);
        return new ResponseData(dto);
    }

    @RequestMapping(value = "/hap/om/order/headers/remove2")
    @ResponseBody
    public ResponseData delete2(HttpServletRequest request, @RequestBody List<OmOrderHeaders> dto, Long headerid) {
        service.batchDelete(dto);
        OmOrderLines omOrderLines = new OmOrderLines();
        omOrderLines.setHeaderId(headerid);
        lservice.deleteByPrimaryKey(omOrderLines);
        return new ResponseData();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private void processOmOrderLines(OmOrderHeaders omOrderHeaders) {
        for (OmOrderLines omOrderLines : omOrderHeaders.getOmOrderLiness()) {
            if (omOrderLines.getLineId() == null) {
                omOrderLines.setHeaderId(omOrderHeaders.getHeaderId());
                omOrderLinesMapper.insertSelective(omOrderLines);
            } else {
                omOrderLinesMapper.updateByPrimaryKeySelective(omOrderLines);
            }
        }
    }

    @RequestMapping(value = "/hap/om/order/headers/exportExcel")
    @ResponseBody
    public void exportExcel(OmOrderHeaders dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request, HttpServletResponse response) {
        IRequest requestContext = createRequestContext(request);
        service.exportExcel(requestContext, dto, page, pageSize, response);
    }

    @RequestMapping(value = "/hap/om/order/headers/setVirtualData")
    public void setVirtualData(@RequestParam String inventoryItemId) {
        if (getList() != null) {
            setList(null);
        }
        System.out.println(inventoryItemId);
        OmOrderLines omOrderLines = new OmOrderLines();
        omOrderLines.setInventoryItemId(Long.valueOf(inventoryItemId));
        List<OmOrderLines> linesList = omOrderLinesMapper.select(omOrderLines);
        List<OmOrderHeaders> list = new ArrayList<>();
        OmOrderHeaders omOrderHeaders = new OmOrderHeaders();


        Set set = new HashSet();
        for (OmOrderLines cd : linesList) {
            long heardId = cd.getHeaderId();
            if (set.add(heardId)) {
                omOrderHeaders.setHeaderId(heardId);
                list.add(omOrderHeaders);
            }
        }

        setList(list);

    }

    @RequestMapping(value = "/hap/om/order/headers/query")
    @ResponseBody
    @ExcelExport(table = OmOrderHeaders.class)
    public ResponseData query(OmOrderHeaders dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        if (getList() != null) {
            int flag = 0;
            List<OmOrderHeaders> list1 = service.select(requestContext, dto, page, pageSize);
            List<OmOrderHeaders> list2 = new ArrayList<>();
            for (int i = 0; i < list1.size(); i++) {
                for (int j = 0; j < getList().size(); j++) {
                    if (list1.get(i).getHeaderId() == getList().get(j).getHeaderId()) {
                        list2.add(list1.get(j));
                        break;
                    }
                }
            }

            setList(null);
            return new ResponseData(list2);
        } else {
            return new ResponseData(service.select(requestContext, dto, page, pageSize));
        }
    }

    @RequestMapping(value = "/hap/om/order/headers/pdfPrint")
    @ResponseBody
    public void printPdf(HttpServletResponse response, Long headerId) {

        service.printPDF(headerId, response);
    }


    @RequestMapping(value = "/hap/om/order/headers/query2")
    @ResponseBody
    @ExcelExport(table = OmOrderHeaders.class)
    public ResponseData query2(OmOrderHeaders dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        List<OmOrderHeaders>list=service.select2(requestContext, dto, page, pageSize);
        return new ResponseData(service.select2(requestContext, dto, page, pageSize));
    }
}