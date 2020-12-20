package org.cch.napa.samples;

import org.cch.napa.Client;
import org.cch.napa.entity.EntityDao;
import org.cch.napa.entity.EntityDaoFactory;
import org.cch.napa.entity.EntityDaoFactoryHelper;
import org.cch.napa.entity.TableManager;
import org.cch.napa.annotations.*;
import org.cch.napa.exceptions.PersistenceException;

import java.util.Date;

public class Setup {
    public static void main(String...args) throws PersistenceException {
        Client client = new Client("jdbc:sqlite:sample.db");
        client.getTableManager().createTableIfNotExisting(SampleEntity.class);
        EntityDao<SampleEntity> dao = client.getEntityDao(SampleEntity.class);
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
