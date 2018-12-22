package com.transworldtechnology.crm.upload;

import java.util.Map;

/**
 * Created by root on 8/4/16.
 */
public interface MeUploadable {
    Map<String, Object> saveContactsToServer() throws Exception;

    Map<String, Object> saveFollowUpsToServer() throws Exception;
}
