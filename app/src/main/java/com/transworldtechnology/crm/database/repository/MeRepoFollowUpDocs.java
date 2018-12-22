package com.transworldtechnology.crm.database.repository;

import java.util.Map;

/**
 * Created by root on 5/4/16.
 */
public interface MeRepoFollowUpDocs {
    void saveFollowUpDocsLocal(Map<String, Object> mapEntity) throws Exception;

    int getMaxSrNo();


}
