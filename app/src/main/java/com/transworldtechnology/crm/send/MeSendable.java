package com.transworldtechnology.crm.send;

import java.util.Map;

/**
 * Created by root on 18/4/16.
 */
public interface MeSendable {
    Map<String, Object> sendLocation() throws Exception;
}
