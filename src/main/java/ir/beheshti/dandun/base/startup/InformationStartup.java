package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.user.entity.InformationEntity;
import ir.beheshti.dandun.base.user.repository.InformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class InformationStartup implements Insert {

    @Autowired
    private InformationRepository informationRepository;

    @Override
    public void insert() {
        InformationEntity entity1 = new InformationEntity();
        entity1.setTitle("title1");
        entity1.setDescription("description description description description " +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description");

        InformationEntity entity2 = new InformationEntity();
        entity2.setTitle("title2");
        entity2.setDescription("description description description description " +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description");

        InformationEntity entity3 = new InformationEntity();
        entity3.setTitle("title3");
        entity3.setDescription("description description description description " +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description");

        InformationEntity entity4 = new InformationEntity();
        entity4.setTitle("title4");
        entity4.setDescription("description description description description " +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description");

        InformationEntity entity5 = new InformationEntity();
        entity5.setTitle("title1");
        entity5.setDescription("description description description description " +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description" +
                "" +
                "description description description description");

        informationRepository.saveAll(Arrays.asList(entity1, entity2, entity3, entity4, entity5));
    }
}
