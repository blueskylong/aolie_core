package com.ranranx.aolie.handler.param;

/**
 * @Author xxl
 * @Description
 * @Date 2020/8/8 20:02
 * @Version V0.0.1
 **/
public class Page {
    private int currentPage;
    private int totalRecord;
    private int pageSize;

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
}
