package com.jornah.service;

import com.jornah.dao.ArticleDao;
import com.jornah.dao.DraftDao;
import com.jornah.model.entity.Article;
import com.jornah.model.entity.Draft;
import com.jornah.utils.TextDifferenceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DraftServiceImpl implements DraftService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private DraftDao draftDao;

    @Override
    public int createDraft(long arId, String newContent, Integer status) {
        Article article = articleDao.selectById(arId);
        String content = "";
        if (Objects.nonNull(article)) {
            content = article.getContent();
        }
        String diffText = TextDifferenceChecker.getDiff(content, newContent);
        Draft draft = new Draft(arId, diffText, status);
        return draftDao.insert(draft);
    }

    @Override
    public int createDraft(long contentId, String originalContent, String newContent, Integer status) {
        String diffText = TextDifferenceChecker.getDiff(originalContent, newContent);
        Draft draft = new Draft(contentId, diffText, status);
        return draftDao.insert(draft);
    }

    @Override
    public int createDraftForDelete(long arId, Integer status) {
        String content = articleDao.selectById(arId).getContent();
        String diffText = TextDifferenceChecker.getDiff(content, "");
        Draft draft = new Draft(arId, diffText, status);
        return draftDao.insert(draft);
    }
}
