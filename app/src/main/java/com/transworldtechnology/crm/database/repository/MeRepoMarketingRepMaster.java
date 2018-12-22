package com.transworldtechnology.crm.database.repository;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 4/4/16.
 */
public interface MeRepoMarketingRepMaster {
    void saveAssignToLocally(List<Map<String, Object>> listAssignTo) throws Exception;

    void deleteTableData() throws Exception;

    List<String> getAssignToList() throws Exception;
}
