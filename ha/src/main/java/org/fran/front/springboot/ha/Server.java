package org.fran.front.springboot.ha;

public class Server {
    long id;
    String url;
    boolean state;
    boolean delete;
    String path;
    String expectContent;

    public Server(long id, String url) {
        this.id = id;
        this.url = url;
        state = true;
        delete = false;
    }

    public Server(long id, String url, String path, String expectContent) {
        this.id = id;
        this.url = url;
        this.path = path;
        this.expectContent = expectContent;
        state = true;
        delete = false;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public boolean getStage() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getPath() {
        return path == null? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExpectContent() {
        return expectContent;
    }

    public void setExpectContent(String expectContent) {
        this.expectContent = expectContent;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", state=" + state +
                ", delete=" + delete +
                ", path='" + path + '\'' +
                ", expectContent='" + expectContent + '\'' +
                '}';
    }
}