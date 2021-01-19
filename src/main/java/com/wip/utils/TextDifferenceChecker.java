package com.wip.utils;

import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class TextDifferenceChecker {
    public static void main(String[] args) throws IOException {
        String diff = getDiff("12123", "2231231");
        System.out.println(diff);
    }

    // 如何读懂 git diff 的结果 http://www.ruanyifeng.com/blog/2012/08/how_to_read_diff.html
    public static String getDiff(String from ,String to){
        RawText before = new RawText(from.getBytes());
        RawText after = new RawText(to.getBytes());
        EditList diff = DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS).diff(RawTextComparator.DEFAULT, before, after);

        ByteArrayOutputStream diffOriginOut=new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(diffOriginOut);
        try {
            diffFormatter.format(diff, before, after);
        } catch (IOException e) {
            return e.getMessage();
        }
        return diffOriginOut.toString();

    }


}
