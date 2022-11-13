public class PageEntryBuilder {

    private String pdfName;
    private int page;
    private int count;

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PageEntry build() {
        return new PageEntry(pdfName, page, count);
    }
}
