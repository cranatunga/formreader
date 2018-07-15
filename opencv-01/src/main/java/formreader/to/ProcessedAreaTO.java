package formreader.to;

public class ProcessedAreaTO extends AreaTO {

    private String content;

    public static ProcessedAreaTO of(AreaTO areaTO) {
        ProcessedAreaTO processedAreaTO = new ProcessedAreaTO();
        processedAreaTO.setTop(areaTO.getTop());
        processedAreaTO.setLeft(areaTO.getLeft());
        processedAreaTO.setHeight(areaTO.getHeight());
        processedAreaTO.setWidth(areaTO.getWidth());

        return processedAreaTO;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}