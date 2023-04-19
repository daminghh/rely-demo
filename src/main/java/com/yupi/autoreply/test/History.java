package com.yupi.autoreply.test;


import com.yupi.autoreply.model.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daming
 */
public class History {
    public static Map<String, List<Message>> historys = new ConcurrentHashMap<>();
}
