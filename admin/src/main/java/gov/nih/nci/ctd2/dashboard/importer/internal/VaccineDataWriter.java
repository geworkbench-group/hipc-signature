package gov.nih.nci.ctd2.dashboard.importer.internal;

import gov.nih.nci.ctd2.dashboard.dao.DashboardDao;
import gov.nih.nci.ctd2.dashboard.model.Vaccine;
import gov.nih.nci.ctd2.dashboard.util.StableURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component("vaccineDataWriter")
public class VaccineDataWriter implements ItemWriter<Vaccine> {

    @Autowired
	private DashboardDao dashboardDao;
 
	private static final Log log = LogFactory.getLog(VaccineDataWriter.class);

    @Autowired
    @Qualifier("batchSize")
    private Integer batchSize;
 
	public void write(List<? extends Vaccine> items) throws Exception {
        for(Vaccine vaccine: items) {
            String stableURL = new StableURL().createURLWithPrefix("vaccine", vaccine.getVaccineID());
            vaccine.setStableURL(stableURL);
        }
        dashboardDao.batchSave(items, batchSize);
        log.debug("vaccine saved");
	}
}
