package cn.trunch.weidong.entity;

import java.io.Serializable;

public class PageEntity  implements Serializable {
    private Integer count;
    private Integer currentPage;
    private Integer fromIndex;
    private Boolean nextPage;
    private Integer pageSize;

    public PageEntity() {
        this.currentPage = 0;
        this.pageSize = 5;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageEntity{" +
                "count=" + count +
                ", currentPage=" + currentPage +
                ", fromIndex=" + fromIndex +
                ", nextPage=" + nextPage +
                ", pageSize=" + pageSize +
                '}';
    }
}
