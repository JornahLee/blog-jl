package com.wip.service;

import com.wip.dao.ContentDao;
import com.wip.dao.DraftDao;
import com.wip.model.Content;
import com.wip.model.Draft;
import com.wip.utils.TextDifferenceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DraftServiceImpl implements DraftService {
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private DraftDao draftDao;

    @Override
    public int createDraft(long contentId, String newContent, Integer status) {
        Content article = contentDao.getArticleById(new Long(contentId).intValue());
        String content = "";
        if (Objects.nonNull(article)) {
            content = article.getContent();
        }
        String diffText = TextDifferenceChecker.getDiff(content, newContent);
        Draft draft = new Draft(contentId, diffText, status);
        return draftDao.insert(draft);
    }

    @Override
    public int createDraft(long contentId, String originalContent, String newContent, Integer status) {
        String diffText = TextDifferenceChecker.getDiff(originalContent, newContent);
        Draft draft = new Draft(contentId, diffText, status);
        return draftDao.insert(draft);
    }

    @Override
    public int createDraftForDelete(long contentId, Integer status) {
        String content = contentDao.getArticleById(new Long(contentId).intValue()).getContent();
        String diffText = TextDifferenceChecker.getDiff(content, "");
        Draft draft = new Draft(contentId, diffText, status);
        return draftDao.insert(draft);
    }
}
