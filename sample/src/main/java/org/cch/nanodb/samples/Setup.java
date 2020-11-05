package org.cch.nanodb.samples;

import org.cch.nanodb.EntityDao;
import org.cch.nanodb.EntityDaoFactory;
import org.cch.nanodb.EntityDaoFactoryHelper;
import org.cch.nanodb.TableCreationUtil;
import org.cch.nanodb.annotations.*;
import org.cch.nanodb.exceptions.PersistenceException;

import java.util.Date;

public class Setup {
    public static void main(String...args) throws PersistenceException {
        EntityDaoFactory factory =EntityDaoFactoryHelper.getFactoryFromConnectionString("jdbc:sqlite:sample.db");
        TableCreationUtil tableCreationUtil = new TableCreationUtil(factory.getDefaultConnectionProvider(), factory);
        tableCreationUtil.createTableIfNotExisting(SampleEntity.class);
        EntityDao<SampleEntity> dao = factory.getEntityDao(SampleEntity.class);
        dao.insert(new SampleEntity());
        for(SampleEntity ent : dao.selectAll()) {

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
