package com.wip.utils.commonmark;

import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HeadingRenderer implements NodeRenderer {
    private final HtmlWriter html;
    private HtmlNodeRendererContext context;

    public HeadingRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(Heading.class);
    }

    @Override
    public void render(Node node) {
        Heading headBlock = (Heading) node;
        int level = headBlock.getLevel();
        HashMap<String, String> attr = new HashMap<>();
        Map<String, String> attrs = getAttrs(headBlock, "h" + level);
        attr.put("href", "#" + attrs.get("id"));
        attr.put("style", "display:block;border: none;");
        String prefix="";
        switch (level){
            case 1:prefix="";break;
            case 2:prefix="——";break;
            case 3:prefix="————";break;
            case 4:prefix="——————";break;
            case 5:prefix="————————";break;
        }
        html.tag("h"+level);
        html.tag("a",attr);
        html.text( prefix+attrs.get("id"));
        html.tag("/a");
        html.tag("/h"+level);
        html.line();

    }
    private Map<String, String> getAttrs(Node node, String tagName) {
        return getAttrs(node, tagName, Collections.<String, String>emptyMap());
    }
    private Map<String, String> getAttrs(Node node, String tagName, Map<String, String> defaultAttributes) {
        return context.extendAttributes(node, tagName, defaultAttributes);
    }
}
