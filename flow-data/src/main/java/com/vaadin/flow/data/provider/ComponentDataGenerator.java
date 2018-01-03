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
package com.vaadin.flow.data.provider;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.renderer.ComponentTemplateRenderer;

import elemental.json.JsonObject;

/**
 * A {@link DataGenerator} that manages the creation and passivation of
 * components generated by {@link ComponentTemplateRenderer}s. It also manages
 * the generation of the {@code nodeId} property needed by the
 * {@code flow-component-renderer} webcomponent.
 * <p>
 * This class is used internally by listing components that support
 * ComponentRenderers.
 * 
 * @author Vaadin Ltd.
 *
 * @param <T>
 *            the date type
 */
public class ComponentDataGenerator<T> implements DataGenerator<T> {

    private final ComponentTemplateRenderer<? extends Component, T> componentRenderer;
    private final Map<String, Component> renderedComponents;
    private final Element container;
    private final String nodeIdPropertyName;
    private final DataKeyMapper<T> keyMapper;

    /**
     * Creates a new generator.
     * 
     * @param componentRenderer
     *            the renderer used to produce components based on data items
     * @param container
     *            the element used as parent for all generated components
     * @param nodeIdPropertyName
     *            the property name used in the JSON to transmit the nodeId of
     *            the generated component to the client
     * @param keyMapper
     *            the DataKeyMapper used to fetch keys for items
     */
    public ComponentDataGenerator(
            ComponentTemplateRenderer<? extends Component, T> componentRenderer,
            Element container, String nodeIdPropertyName,
            DataKeyMapper<T> keyMapper) {
        this.componentRenderer = componentRenderer;
        this.container = container;
        this.nodeIdPropertyName = nodeIdPropertyName;
        this.keyMapper = keyMapper;

        renderedComponents = new HashMap<>();
    }

    @Override
    public void generateData(T item, JsonObject jsonObject) {
        String itemKey = jsonObject.getString("key");
        Component oldRenderedComponent = renderedComponents.get(itemKey);
        Component renderedComponent = componentRenderer.createComponent(item);
        if (oldRenderedComponent != renderedComponent) {
            if (oldRenderedComponent != null) {
                oldRenderedComponent.getElement().removeFromParent();
            }
            registerRenderedComponent(itemKey, renderedComponent);
        }
        int nodeId = renderedComponent.getElement().getNode().getId();
        jsonObject.put(nodeIdPropertyName, nodeId);
    }

    @Override
    public void refreshData(T item) {
        String itemKey = keyMapper.key(item);
        Component oldComponent = renderedComponents.get(itemKey);
        if (oldComponent != null) {
            Component recreatedComponent = componentRenderer
                    .createComponent(item);

            int oldId = oldComponent.getElement().getNode().getId();
            int newId = recreatedComponent.getElement().getNode().getId();
            if (oldId != newId) {
                container.removeChild(oldComponent.getElement());
                registerRenderedComponent(itemKey, recreatedComponent);
            }
        }
    }

    @Override
    public void destroyData(T item) {
        String itemKey = keyMapper.key(item);
        Component renderedComponent = renderedComponents.remove(itemKey);
        if (renderedComponent != null) {
            renderedComponent.getElement().removeFromParent();
        }
    }

    @Override
    public void destroyAllData() {
        container.removeAllChildren();
        renderedComponents.clear();
    }

    private void registerRenderedComponent(String itemKey,
            Component component) {

        Element element = component.getElement();
        container.appendChild(element);
        renderedComponents.put(itemKey, component);
    }

}