package com.ranranx.aolie.core.handler.param;

/**
 * @author xxl
 *
 * @date 2020/8/8 20:02
 * @version V0.0.1
 **/
public class Page {
    private int currentPage;
    private int totalRecord;
    private int pageSize;
    private int totalPage;

    public Page(int currentPage, int totalRecord, int pageSize) {
        this.currentPage = currentPage;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = new Double(Math.ceil((0.0 + this.totalRecord) / this.pageSize)).intValue();
    }

    public Page() {
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
