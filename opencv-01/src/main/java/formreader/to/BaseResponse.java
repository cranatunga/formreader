package formreader.to;

public class BaseResponse {

    private Boolean success;

    public BaseResponse() {
        success = true;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}