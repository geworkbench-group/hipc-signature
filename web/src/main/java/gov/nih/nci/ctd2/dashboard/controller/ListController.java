package gov.nih.nci.ctd2.dashboard.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import flexjson.JSONSerializer;
import gov.nih.nci.ctd2.dashboard.dao.DashboardDao;
import gov.nih.nci.ctd2.dashboard.model.DashboardEntity;
import gov.nih.nci.ctd2.dashboard.model.Submission;
import gov.nih.nci.ctd2.dashboard.util.DateTransformer;
import gov.nih.nci.ctd2.dashboard.util.ImplTransformer;
import gov.nih.nci.ctd2.dashboard.util.PMIDResult;
import gov.nih.nci.ctd2.dashboard.util.WebServiceUtil;

@Controller
@RequestMapping("/list")
public class ListController {
    @Autowired
    private DashboardDao dashboardDao;

    @Autowired
    private WebServiceUtil webServiceUtil;

    public WebServiceUtil getWebServiceUtil() {
        return webServiceUtil;
    }

    public void setWebServiceUtil(WebServiceUtil webServiceUtil) {
        this.webServiceUtil = webServiceUtil;
    }

    @Transactional
    @RequestMapping(value = "pmids", method = { RequestMethod.GET,
            RequestMethod.POST }, headers = "Accept=application/json")
    public ResponseEntity<String> getAllPMIDs() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        List<PMIDResult> pmids = dashboardDao.getPMIDs();
        JSONSerializer jsonSerializer = new JSONSerializer().exclude("class")
                .transform(new DateTransformer(), Date.class);
        return new ResponseEntity<String>(jsonSerializer.serialize(pmids), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "submission/{pmid}", method = { RequestMethod.GET,
            RequestMethod.POST }, headers = "Accept=application/json")
    public ResponseEntity<String> getSubmissionsPerPMID(@PathVariable Integer pmid) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        List<Submission> submissions = dashboardDao.getSubmissionsPerPMID(pmid);
        JSONSerializer jsonSerializer = new JSONSerializer().exclude("class")
                .transform(new DateTransformer(), Date.class);
        return new ResponseEntity<String>(jsonSerializer.serialize(submissions), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "{type}", method = { RequestMethod.GET,
            RequestMethod.POST }, headers = "Accept=application/json")
    public ResponseEntity<String> getSearchResultsInJson(@PathVariable String type,
            @RequestParam("filterBy") Integer filterBy,
            @RequestParam(value = "getAll", required = false, defaultValue = "false") Boolean getAll) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        List<? extends DashboardEntity> entities = webServiceUtil.getDashboardEntities(type, filterBy);

        JSONSerializer jsonSerializer = new JSONSerializer().transform(new ImplTransformer(), Class.class)
                .transform(new DateTransformer(), Date.class);

        String s = null;
        if ("template".equals(type)) {
            s = jsonSerializer.deepSerialize(entities);
        } else {
            s = jsonSerializer.serialize(entities);
        }
        return new ResponseEntity<String>(s, headers, HttpStatus.OK);
    }
}
