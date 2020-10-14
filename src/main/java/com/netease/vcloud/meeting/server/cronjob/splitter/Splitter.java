package com.netease.vcloud.meeting.server.cronjob.splitter;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Splitter<T> {

    public static final String MEETING_INFO_REDIS_KEY = "meeting_info_notify_key";

    public abstract List<T> query(long timestamp);

    public List<T> split(int index, int total, long timestamp) {
        List<T> list = query(timestamp);
        return split(list, index, total);
    }

    private List<T> split(List<T> list, int index, int total) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        int count = list.size();
        int lastCount = count % total;
        int countCols = count / total;
        Map<String, List<T>> ret = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            int fromIndex;
            int toIndex;
            if (i == total) {
                fromIndex = (i - 1) * countCols;
                toIndex = i * countCols + lastCount;
            } else {
                fromIndex = (i - 1) * countCols;
                toIndex = i * countCols;
            }
            if (toIndex > 0) {
                List<T> subList = list.subList(fromIndex, toIndex);
                ret.put(String.valueOf(i), subList);
            }
        }
        return ret.get(String.valueOf(index + 1));
    }
}
