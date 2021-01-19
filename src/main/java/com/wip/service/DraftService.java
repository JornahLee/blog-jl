package com.wip.service;

import com.wip.model.Draft;

public interface DraftService {
    Draft createDraft(long contentId, String from , String to);
}
