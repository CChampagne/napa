package org.cch.napa.samples;

import org.cch.napa.EntityDao;
import org.cch.napa.EntityDaoFactory;
import org.cch.napa.EntityDaoFactoryHelper;
import org.cch.napa.TableCreationUtil;
import org.cch.napa.annotations.*;
import org.cch.napa.exceptions.PersistenceException;

import java.util.Date;

public class Setup {
    public static void main(String...args) throws PersistenceException {
        EntityDaoFactory factory =EntityDaoFactoryHelper.getFactoryFromConnectionString("jdbc:sqlite:sample.db");
        TableCreationUtil tableCreationUtil = new TableCreationUtil(factory.getDefaultConnectionProvider(), factory);
        tableCreationUtil.createTableIfNotExisting(SampleEntity.class);
        EntityDao<SampleEntity> dao = factory.getEntityDao(SampleEntity.class);
        dao.insert(new SampleEntity());
        for(SampleEntity entity : dao.selectAll()) {

        }
    }
    @Entity(table = "sample")
    public static class SampleEntity {
        @Id
        @GeneratedValue
        private long id;

        @GeneratedValue()
        private Date creationDate;
    }
}
