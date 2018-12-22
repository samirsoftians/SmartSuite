package com.transworldtechnology.crm.pdf;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by melayer on 17/3/16.
 */
public interface MePdfCreatable {
    File initConfigLedger(List<Map<String, Object>> mapLedgerAccountDetails);

    File initConfigInvoice(List<Map<String, Object>> listInvoiceMap);
}
