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
package com.vaadin.flow.router;

import com.vaadin.flow.router.internal.BeforeEnterHandler;

/**
 * Any {@code com.vaadin.ui.Component} implementing this interface will be
 * informed when they are being attached to the
 * {@link com.vaadin.flow.component.UI}.
 * <p>
 * During this phase there is the possibility to reroute to another navigation
 * target.
 *
 * @author Vaadin Ltd
 */
@FunctionalInterface
public interface BeforeEnterObserver extends BeforeEnterHandler {
}
