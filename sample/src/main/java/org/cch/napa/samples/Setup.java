package org.cch.napa.samples;

import org.cch.napa.Client;
import org.cch.napa.entity.EntityDao;
import org.cch.napa.entity.annotations.*;
import org.cch.napa.exceptions.PersistenceException;

import java.util.Date;
import java.util.UUID;

public class Setup {
    public static void main(String...args) throws PersistenceException {
        Client client = new Client("jdbc:sqlite:sample.db");
        client.getTableManager().createTableIfNotExisting(SampleEntity.class);
        EntityDao<SampleEntity> dao = client.getEntityDao(SampleEntity.class);

        SampleEntity entity = new SampleEntity();
        entity.setContent("Test message");
        dao.insert(entity);

        for(SampleEntity readEntity : dao.selectAll()) {
            System.out.println("Message " + readEntity.getId() + ": " + readEntity.getContent());
            System.out.println("Created on " + readEntity.getCreationDate());
            System.out.println("Last updated on " + readEntity.getLastModificationDate());
        }
    }
    @Entity(table = "sample")
    public static class SampleEntity {

        @Id
        @GeneratedValue
        private String id;
        private Date creationDate;
        private Date lastModificationDate;
        private String content;

        public String getId() {
            return id;
        }

        //Field annotations can also be associated to accessors
        @GeneratedValue
        public Date getCreationDate() {
            return creationDate;
        }

        @GeneratedValue(generateAlsoOnUpdate = true)
        public Date getLastModificationDate() {
            return lastModificationDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
