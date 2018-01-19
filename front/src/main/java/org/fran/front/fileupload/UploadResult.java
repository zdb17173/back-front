package org.fran.front.fileupload;

import org.fran.front.common.BaseResult;

/**
 * Created by leon on 2016/11/2.
 */
public class UploadResult extends BaseResult {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
