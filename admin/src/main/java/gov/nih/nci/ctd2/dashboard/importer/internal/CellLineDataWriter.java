package gov.nih.nci.ctd2.dashboard.importer.internal;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import gov.nih.nci.ctd2.dashboard.util.StableURL;
import gov.nih.nci.ctd2.dashboard.dao.DashboardDao;
import gov.nih.nci.ctd2.dashboard.model.CellSample;
import gov.nih.nci.ctd2.dashboard.model.DashboardEntity;

public class CellLineDataWriter implements Tasklet {

    @Autowired
	private DashboardDao dashboardDao;

    @Autowired
	@Qualifier("cellSampleMap")
	private HashMap<String,CellSample> cellSampleMap;

    @Autowired
    @Qualifier("batchSize")
    private Integer batchSize;

	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        ArrayList<DashboardEntity> entities = new ArrayList<DashboardEntity>();
        for (CellSample cellSample : cellSampleMap.values()) {
            entities.addAll(cellSample.getAnnotations());
            String stableURL = new StableURL().createURLWithPrefix("cell-sample", cellSample.getDisplayName());
            cellSample.setStableURL(stableURL);
            entities.add(cellSample);
        }
        dashboardDao.batchSave(entities, batchSize);
        return RepeatStatus.FINISHED;
    }
}
