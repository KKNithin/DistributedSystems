package service.messages;

import service.core.QuotationService;

public class Init {
    private QuotationService service;

    public Init(QuotationService service) {
        this.service = service;
    }

    public Init() {
    }

    public QuotationService getService() {
        return service;
    }

    public void setService(QuotationService service) {
        this.service = service;
    }
}
