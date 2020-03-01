package com.werun.back.entity;

import java.io.Serializable;

/**
 * @ClassName PageInfo
 * @Author HWG
 * @Time 2019/4/20 8:30
 */

public class PageInfo implements Serializable {
    private int count;
    private int fromIndex;
    private int currentPage;
    private int pageSize;
    private boolean nextPage;

    public PageInfo(int count, int currentPage, int pageSize) {
        this.count = count;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.fromIndex=this.pageSize*(this.currentPage-1);
        if ((this.count > (this.fromIndex + this.pageSize))) {
            this.nextPage = true;
        } else {
            this.nextPage = false;
        }


    }

    public PageInfo() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }
}
