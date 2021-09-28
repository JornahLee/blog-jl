package com.jornah.service;

public interface DraftService {
    int createDraft(long contentId,  String newContent, Integer status);

    int createDraft(long contentId, String originalContent, String newContent, Integer status);

    int createDraftForDelete(long contentId, Integer status);
}
