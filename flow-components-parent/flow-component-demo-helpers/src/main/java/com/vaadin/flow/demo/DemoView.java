/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.Route;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HasComponents;
import com.vaadin.ui.common.HasStyle;
import com.vaadin.ui.common.JavaScript;
import com.vaadin.ui.common.StyleSheet;
import com.vaadin.ui.event.AttachEvent;
import com.vaadin.ui.html.Div;
import com.vaadin.ui.html.H3;

/**
 * Base class for all the Views that demo some component.
 * 
 * @author Vaadin Ltd
 */
@Tag(Tag.DIV)
@StyleSheet("src/css/demo.css")
@JavaScript("src/script/prism.js")
public abstract class DemoView extends Component
        implements HasComponents, HasUrlParameter<String>, HasStyle {

    private DemoNavigationBar navBar = new DemoNavigationBar();
    private Div container = new Div();

    private Map<String, Div> tabComponents = new HashMap<>();
    private Map<String, List<SourceCodeExample>> sourceCodeExamples = new HashMap<>();

    protected DemoView() {
        Route annotation = getClass().getAnnotation(Route.class);
        if (annotation == null) {
            throw new IllegalStateException(
                    getClass().getName() + " should be annotated with @"
                            + Route.class.getName() + " to be a valid view");
        }
        addClassName("demo-view");
        navBar.addClassName("demo-nav");
        add(navBar);
        add(container);

        populateSources();
        initView();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (tabComponents.size() <= 1) {
            remove(navBar);
        }
    }

    /**
     * Method run where the actual view builds its contents.
     */
    protected abstract void initView();

    /**
     * When called the view should populate the given SourceContainer with
     * sample source code to be shown.
     */
    public void populateSources() {
        SourceContentResolver.getSourceCodeExamplesForClass(getClass())
                .forEach(this::putSourceCode);
    }

    private void putSourceCode(SourceCodeExample example) {
        String heading = example.getHeading();
        List<SourceCodeExample> list = sourceCodeExamples
                .computeIfAbsent(heading, key -> new ArrayList<>());
        list.add(example);
    }

    /**
     * Creates and adds a new component card to the "Basic usage" tab in the
     * view. It automatically adds any source code examples with the same
     * heading to the bottom of the card.
     *
     * @param heading
     *            the header text of the card, that is added to the layout. If
     *            <code>null</code> or empty, the header is not added
     *
     * @param components
     *            components to add on creation. If <code>null</code> or empty,
     *            the card is created without the components inside
     * @return created component container card
     * @see #addCard(String, String, Component...)
     */
    public Card addCard(String heading, Component... components) {
        return addCard("Basic usage", "", heading, components);
    }

    /**
     * Creates and adds a new component card to a specific tab in the view. It
     * automatically adds any source code examples with the same heading to the
     * bottom of the card.
     * <p>
     * The href of the tab is defined based on the tab name. For example, a tab
     * named "Advanced usage" has the "advanced-tab" as href (all in lower case
     * and with "-" in place of spaces and special characters).
     * 
     * @param tabName
     *            the name of the tab that will contain the demo, not
     *            <code>null</code>
     * @param heading
     *            the header text of the card, that is added to the layout. If
     *            <code>null</code> or empty, the header is not added
     * @param components
     *            components to add on creation. If <code>null</code> or empty,
     *            the card is created without the components inside
     * @return created component container card
     */
    public Card addCard(String tabName, String heading,
            Component... components) {
        String tabUrl = tabName.toLowerCase().replaceAll("[\\W]", "-");
        return addCard(tabName, tabUrl, heading, components);
    }

    private Card addCard(String tabName, String tabUrl, String heading,
            Component... components) {
        Div tab = tabComponents.computeIfAbsent(tabUrl, url -> {
            navBar.addLink(tabName, getTabUrl(tabUrl));
            return new Div();
        });

        if (heading != null && !heading.isEmpty()) {
            tab.add(new H3(heading));
        }

        Card card = new Card();
        if (components != null && components.length > 0) {
            card.add(components);
        }

        List<SourceCodeExample> list = sourceCodeExamples.get(heading);
        if (list != null) {
            list.stream().map(this::createSourceContent).forEach(card::add);
        }

        tab.add(card);
        return card;
    }

    private String getTabUrl(String relativeHref) {
        String href = relativeHref == null || relativeHref.isEmpty() ? ""
                : "/" + relativeHref;
        return getClass().getAnnotation(Route.class).value() + href;
    }

    private SourceContent createSourceContent(
            SourceCodeExample sourceCodeExample) {
        SourceContent content = new SourceContent();
        String sourceString = sourceCodeExample.getSourceCode();
        switch (sourceCodeExample.getSourceType()) {
        case CSS:
            content.addCss(sourceString);
            break;
        case JAVA:
            content.addCode(sourceString);
            break;
        case UNDEFINED:
        default:
            content.addCode(sourceString);
            break;
        }
        return content;
    }

    private void showTab(String tabUrl) {
        Div tab = tabComponents.get(tabUrl);
        if (tab != null) {
            container.removeAll();
            container.add(tab);
        }
        navBar.setActive(getTabUrl(tabUrl));
        tab.getElement().getNode().runWhenAttached(
                ui -> ui.getPage().executeJavaScript("Prism.highlightAll();"));
    }

    @Override
    public void setParameter(BeforeNavigationEvent event,
            @OptionalParameter String parameter) {
        showTab(parameter == null ? "" : parameter);
    }
}
