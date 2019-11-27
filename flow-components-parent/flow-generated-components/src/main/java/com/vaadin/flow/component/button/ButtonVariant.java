/*
 * Copyright 2000-2019 Vaadin Ltd.
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
package com.vaadin.flow.component.button;

import javax.annotation.Generated;

/**
 * Set of theme variants applicable for {@code vaadin-button} component.
 */
@Generated({ "Generator: com.vaadin.generator.ComponentGenerator#1.2-SNAPSHOT",
        "WebComponent: Vaadin.ButtonElement#2.1.0", "Flow#1.2-SNAPSHOT" })
public enum ButtonVariant {
    LUMO_SMALL("small"), LUMO_LARGE("large"), LUMO_TERTIARY(
            "tertiary"), LUMO_TERTIARY_INLINE("tertiary-inline"), LUMO_PRIMARY(
                    "primary"), LUMO_SUCCESS("success"), LUMO_ERROR(
                            "error"), LUMO_CONTRAST("contrast"), LUMO_ICON(
                                    "icon"), MATERIAL_CONTAINED(
                                            "contained"), MATERIAL_OUTLINED(
                                                    "outlined");

    private final String variant;

    ButtonVariant(String variant) {
        this.variant = variant;
    }

    /**
     * Gets the variant name.
     * 
     * @return variant name
     */
    public String getVariantName() {
        return variant;
    }
}