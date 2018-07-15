package formreader.to.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("formreader")
public class FormReaderConfig {

    private PathConfig path;

    public PathConfig getPath() {
        return path;
    }

    public void setPath(PathConfig path) {
        this.path = path;
    }
}