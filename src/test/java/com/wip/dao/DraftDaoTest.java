package com.wip.dao;


import com.wip.model.Draft;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@SpringBootTest
public class DraftDaoTest {
    @Autowired
    DraftDao draftDao;

    @Test
    public void insert(){
        Draft draft = new Draft();
        draft.setContentId(1);
        draft.setDiffText("123123");
        draft.setCreated(Instant.now());
        draftDao.insert(draft);
    }


}