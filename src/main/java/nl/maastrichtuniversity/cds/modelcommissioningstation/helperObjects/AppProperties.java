package nl.maastrichtuniversity.cds.modelcommissioningstation.helperObjects;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppProperties {
    /////////////////////////Validation repository properties/////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    @Value("${validation.enabled}")
    @Getter private boolean validationEnabled;

    @Value("${validation.repoType}")
    @Getter private String validationRepoType;

    @Value("${validation.repoUrl}")
    @Getter private String validationRepoUrl;

    @Value("${validation.repoId}")
    @Getter private String validationRepoId;

    @Value("${validation.repoUser}")
    @Getter private String validationRepoUser;

    @Value("${validation.repoPass}")
    @Getter private String validationRepoPass;

    /////////////////////////Model cache repository properties/////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    @Value("${model.repoType}")
    @Getter private String modelRepoType;

    @Value("${model.repoUrl}")
    @Getter private String modelRepoUrl;

    @Value("${model.repoId}")
    @Getter private String modelRepoId;

    @Value("${model.repoUser}")
    @Getter private String modelRepoUser;

    @Value("${validation.repoPass}")
    @Getter private String modelRepoPass;
}
