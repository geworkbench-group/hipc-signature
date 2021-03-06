package gov.nih.nci.ctd2.dashboard.importer.internal;

import gov.nih.nci.ctd2.dashboard.dao.DashboardDao;
import gov.nih.nci.ctd2.dashboard.model.AnimalModel;
import gov.nih.nci.ctd2.dashboard.util.StableURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component("animalModelWriter")
public class AnimalModelWriter implements ItemWriter<AnimalModel> {

    @Autowired
	private DashboardDao dashboardDao;
 
	private static final Log log = LogFactory.getLog(AnimalModelWriter.class);

    @Autowired
    @Qualifier("batchSize")
    private Integer batchSize;
 
	public void write(List<? extends AnimalModel> items) throws Exception {
        StableURL stableURL = new StableURL();
        for (AnimalModel animalModel : items) {
            animalModel.setStableURL(stableURL.createURLWithPrefix("animal-model", animalModel.getDisplayName()));
        }
        dashboardDao.batchSave(items, batchSize);
        log.debug("AnimalModel written");
	}
}
